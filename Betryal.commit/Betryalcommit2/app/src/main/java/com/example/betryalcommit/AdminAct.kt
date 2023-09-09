package com.example.betryalcommit

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

lateinit var dpm:DevicePolicyManager
object AdminAct {
    lateinit var deviceAdminReceiver: ComponentName
    fun initialize(context: Context) {
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
    fun lockdev() {
        dpm.lockNow(0);
    }
}