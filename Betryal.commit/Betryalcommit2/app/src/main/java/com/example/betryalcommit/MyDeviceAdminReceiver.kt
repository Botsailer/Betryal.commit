package com.example.betryalcommit
import android.app.ActivityManager
import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
class MyAdminReceiver : DeviceAdminReceiver() {
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        val settingsPackageName = "com.android.settings"
        val packageManager = context.packageManager
        val settingsIntent = packageManager.getLaunchIntentForPackage(settingsPackageName)
        settingsIntent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(settingsIntent)
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(settingsPackageName)
        return "Are you sure you want to Factory reset device?"
    }

}