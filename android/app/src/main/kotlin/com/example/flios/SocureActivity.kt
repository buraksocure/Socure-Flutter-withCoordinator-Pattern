package com.example.flios

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.socure.idplus.SDKAppDataPublic
import com.socure.idplus.model.ScanResult
import com.socure.idplus.scanner.license.LicenseBackScannerActivity
import com.socure.idplus.scanner.license.LicenseFrontScannerActivity
import com.socure.idplus.scanner.license.LicenseScannerActivity
import com.socure.idplus.scanner.passport.PassportScannerActivity
import com.socure.idplus.scanner.selfie.SelfieActivity
import com.socure.idplus.interfaces.Interfaces.UploadCallback
import com.socure.idplus.util.DataInfo
import com.socure.idplus.util.ImageUtil.toBitmap
import com.socure.idplus.devicerisk.androidsdk.Interfaces
import com.socure.idplus.devicerisk.androidsdk.logSDK
import com.socure.idplus.devicerisk.androidsdk.model.InformationRequest
import com.socure.idplus.devicerisk.androidsdk.model.InformationResponse
import com.socure.idplus.devicerisk.androidsdk.model.SocureSdkError
import com.socure.idplus.devicerisk.androidsdk.model.UploadResult
import com.socure.idplus.devicerisk.androidsdk.sensors.DeviceRiskManager
import com.socure.idplus.uploader.InformationUploader

class SocureActivity : AppCompatActivity(),
    DeviceRiskManager.DataUploadCallback  {

    private var sharedPref: SharedPreferences? = null
    private var deviceRiskManager: DeviceRiskManager? = null
    private var uploadResult: UploadResult? = null

    private var result: String? = null
    private var uuid: String? = null

    var allPermissionChecked: Boolean = false

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    )
    companion object {
        const val TAG = "MainActivity"
        private var PRIVATE_MODE = 0
        private val PREF_NAME = "user_preferences.xml"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var status = "status"
        var documentTypeTitle = "document_type_title"
        var autoFilling = "auto_filling"

       val passingIntent = Intent(this@SocureActivity, LicenseScannerActivity::class.java)
            passingIntent.putExtra(status, 0)
            passingIntent.putExtra(documentTypeTitle, "Driver's License")
            passingIntent.putExtra(autoFilling, true)
            startActivityForResult(passingIntent, 300)

            loadDeviceRiskManager()

        }



    private fun loadDeviceRiskManager(){
        deviceRiskManager = DeviceRiskManager()
        val list = mutableListOf<DeviceRiskManager.DeviceRiskDataSourcesEnum>()
        //motion

        list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Device)
        list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Network)

        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        uuid = sharedPref?.getString(getString(R.string.uuidKey), null)

        uuid?.let { logSDK(TAG, it) }

        deviceRiskManager?.setTracker(
            key = "dff2ebfc-fbdf-4d77-80f2-78f9900978f2",
            trackers = list,
            userConsent = true,
            activity = this,
            callback = this,
            deviceRiskUrl = "https://dvnfo.com/"
        )

        deviceRiskManager?.sendData();


    }

    override fun dataUploadFinished(uploadResult: UploadResult) {
        this.uploadResult = uploadResult
        if (uuid == null) {
            this.uploadResult?.uuid?.let {
                logSDK(TAG, it)
                uuid = this.uploadResult?.uuid
                deviceRiskManager?.setUUID(uuid)
                with(sharedPref?.edit()) {
                    this?.putString(getString(R.string.uuidKey), it)
                    this?.commit()
                }
            }
        }

    }

    override fun onError(errorType: DeviceRiskManager.SocureSDKErrorType, errorMessage: String?) {
        if (errorMessage != null) {
            logSDK("device error", errorMessage)
        };

    }




}

