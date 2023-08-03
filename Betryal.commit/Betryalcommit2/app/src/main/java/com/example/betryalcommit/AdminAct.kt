package com.example.betryalcommit

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

lateinit var dpm:DevicePolicyManager

lateinit var deviceAdminReceiver: DeviceAdminReceiver
object AdminAct {
    private lateinit var deviceAdminReceiver: ComponentName
    fun initialize(context: AppCompatActivity) {
        dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        deviceAdminReceiver = ComponentName(context, MyAdminReceiver::class.java)

    }

    fun admact(activity: AppCompatActivity) {
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
    }
    fun isDeviceAdminAssigned(): Boolean {
        return dpm.isAdminActive(deviceAdminReceiver)
    }
    fun wiper(context: Context) {
        //dpm.setApplicationHidden()
    }
    fun factoryResetDevice(context: Context) {
          //  dpm.wipeData(DevicePolicyManager.WIPE_SILENTLY);
           // dpm.lockNow()
        }
    fun lockdev(context: Context) {
       dpm.lockNow(0);

    }
}