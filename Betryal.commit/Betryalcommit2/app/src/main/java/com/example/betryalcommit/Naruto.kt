package com.example.betryalcommit

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class Naruto : AccessibilityService() {
    private lateinit var prefs: SharedPreferences
    private var targetPackageNames: List<String> = emptyList()


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("targetPackages", null)
        targetPackageNames = set?.toList() ?: emptyList()
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.d("TAG", "onAccessibilityEvent: ${event.className}")
            val packageName = event.packageName?.toString()
            if (packageName != null && packageName in targetPackageNames) {
                val intent = Intent(this, blockedapp::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
            else if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                event.packageName?.toString() == "com.android.settings" &&
                event.className?.toString() == "DeviceAdminAdd"
            ) {
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
                 null
    }
}