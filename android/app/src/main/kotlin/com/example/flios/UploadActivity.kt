package com.example.flios


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.error.SocureSdkError
import com.socure.idplus.interfaces.Interfaces.UploadCallback
import com.socure.idplus.model.ScanResult
import com.socure.idplus.model.UploadResult
import com.socure.idplus.upload.ImageUploader
import com.socure.idplus.util.ImageUtil.toBitmap
import com.socure.idplus.util.mergeBooleanWithAnd

class UploadActivity : AppCompatActivity(), UploadCallback {


    private var imageUploader: ImageUploader? = null

    private var uploadSuccessLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var socurePublicKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        try {
            socurePublicKey =
                intent?.getSerializableExtra(resources.getString(R.string.socurePublicKey)) as String
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

        SDKAppDataPublic.successfulScanningResult?.barcodeData?.postalCode =
            SDKAppDataPublic.successfulScanningResult?.barcodeData?.postalCode?.take(5)



        imageUploader = ImageUploader(
            this.baseContext,
            socurePublicKey,
            resources.getString(R.string.uploadUrl)
        )
        imageUploader?.imageUploader(this.baseContext)

        uploadDocuments()

        uploadSuccessLiveData.postValue(false)

        mergeBooleanWithAnd(uploadSuccessLiveData).observe(this, androidx.lifecycle.Observer
        {
            if (it == true) {
                runOnUiThread {

                }
            }
        })
    }

    private fun uploadDocuments() {


        if (SDKAppDataPublic.successfulScanningResult?.passportImage == null && SDKAppDataPublic.successfulScanningResult?.licenseFrontImage == null) {
            Toast.makeText(this, "Front cannot be null", Toast.LENGTH_LONG).show()
        } else
            if (SDKAppDataPublic.successfulScanningResult?.documentType == ScanResult.DocumentType.PASSPORT) {
                if (SDKAppDataPublic.selfieScanResult?.imageData != null) {
                    imageUploader?.uploadPassport(
                        this,
                        SDKAppDataPublic.successfulScanningResult?.passportImage,
                        SDKAppDataPublic.selfieScanResult?.imageData
                    )
                    Log.i(TAG, "uploadPassport with selfie")
                } else {
                    imageUploader?.uploadPassport(
                        this,
                        SDKAppDataPublic.successfulScanningResult?.passportImage
                    )
                    Log.i(TAG, "uploadPassport without selfie")
                }
            } else {
                if (SDKAppDataPublic.selfieScanResult?.imageData != null) {
                    imageUploader?.uploadLicense(
                        this,
                        SDKAppDataPublic.successfulScanningResult?.licenseFrontImage,
                        SDKAppDataPublic.successfulScanningResult?.licenseBackImage,
                        SDKAppDataPublic.selfieScanResult?.imageData
                    )
                    Log.i(TAG, "uploadLicense with selfie")
                } else {
                    imageUploader?.uploadLicense(
                        this,
                        SDKAppDataPublic.successfulScanningResult?.licenseFrontImage,
                        SDKAppDataPublic.successfulScanningResult?.licenseBackImage
                    )
                    Log.i(TAG, "uploadLicense without selfie")
                }
            }
    }

    companion object {
        private val TAG = UploadActivity::class.java.simpleName
    }

    override fun documentUploadFinished(uploadResult: UploadResult?) {
        Log.i(TAG, "documentUploadFinished")
        SDKAppDataPublic.uploadResult = uploadResult
        uploadSuccessLiveData.postValue(true)
    }

    override fun onDocumentUploadError(error: SocureSdkError?) {
        var cancelCause = "onDocumentUploadError"
        error?.let {
            cancelCause = it.toJSON()
        }
        Toast.makeText(this, cancelCause, Toast.LENGTH_SHORT).show()

        uploadSuccessLiveData.postValue(false)

    }

    override fun onSocurePublicKeyError(error: SocureSdkError?) {
        Toast.makeText(this, "onDocumentUploadError: " + error.toString(), Toast.LENGTH_SHORT)
            .show();
        Log.i(TAG, "onSocurePublicKeyError")
        uploadSuccessLiveData.postValue(false)
    }
}