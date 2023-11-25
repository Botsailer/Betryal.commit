package com.example.betryalcommit

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class Naruto : AccessibilityService() {
    private lateinit var prefs: SharedPreferences
    private var targetPackageNames: List<String> = emptyList()


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("targetPackages", null)
        targetPackageNames = set?.toList() ?: emptyList()


        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null && packageName in targetPackageNames) {
                val intent = Intent(this, blockedapp::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else if
                    (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && ("com.android.settings" == packageName && ("com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd" == event.className ))){
                val settingsPackageName = "com.android.settings"
                val packageManager = packageManager
                val settingsIntent = packageManager.getLaunchIntentForPackage(settingsPackageName)
                settingsIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(settingsIntent)
                val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                activityManager.killBackgroundProcesses(settingsPackageName)
            }
        }
    }
    override fun onInterrupt() {

    }
}