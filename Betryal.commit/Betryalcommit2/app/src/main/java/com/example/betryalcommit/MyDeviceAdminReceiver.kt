package com.example.betryalcommit
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Timer

class MyDeviceAdminReceiver : DeviceAdminReceiver() {
    private lateinit var dpm: DevicePolicyManager
    private var current_time: Long = 0
    private lateinit var myThread: Timer

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d("Root", "Device Owner Enabled")
    }
}