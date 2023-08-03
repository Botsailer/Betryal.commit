package com.example.betryalcommit;
import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent

class MyAdminReceiver : DeviceAdminReceiver() {
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        // Intercept the request to disable admin permission

        return "Please enter the password to disable admin permission"

    }

}
