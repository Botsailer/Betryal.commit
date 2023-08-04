package com.example.betryalcommit;
import android.app.AlertDialog
import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.system.Os
import android.system.OsConstants

class MyAdminReceiver : DeviceAdminReceiver() {
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        // Show a confirmation dialog to the user before disabling the Device Administrator
       return super.onDisableRequested(context, intent);
    }
}
