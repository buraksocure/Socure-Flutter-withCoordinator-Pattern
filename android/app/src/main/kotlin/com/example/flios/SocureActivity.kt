package com.example.flios

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import com.socure.idplus.util.DataInfo
import com.socure.idplus.util.ImageUtil.toBitmap

class SocureActivity : AppCompatActivity() {

    var allPermissionChecked: Boolean = false

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE
    )

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
        }







}

