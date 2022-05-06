package com.example.flios

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.flios.SocureActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import io.flutter.embedding.engine.FlutterEngineCache

import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.embedding.engine.FlutterEngine
import androidx.annotation.NonNull

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.MethodCallHandler


class MainActivity: FlutterActivity() {

    private val CHANNEL = "com.socure.flutter/navToSocure"

    val ENGINE_ID = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       startMainActivity() //Direct Call


        val flutterEngine = FlutterEngine(this)
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
        val channel =
            MethodChannel(flutterEngine.dartExecutor, CHANNEL)
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "goToSocure" -> {
                    startMainActivity()
                    result.success(true)
                }
                else -> result.notImplemented()
            }
        }
    }
    private fun startMainActivity() {

        // Start Socure Activity and MoveToBack

        val intent = Intent(this, SocureActivity::class.java)
        startActivity(intent)

        // Start your normal activity

    }


}
