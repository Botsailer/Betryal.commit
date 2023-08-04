package com.example.betryalcommit

import android.app.ActivityManager
import android.content.Context

class hmm {
    companion object {
      fun  isServiceRunning(context: Context, serviceClass: Class<*> = MyService::class.java): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }
    }
}