package com.example.betryalcommit
import android.app.NotificationManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdminAct.initialize(this);

val shared = getSharedPreferences("shared", Context.MODE_PRIVATE)

        val editor = shared.edit()
        if(AdminAct.isDeviceAdminAssigned()){
            editor.putString("current","ok")
            editor.apply()
            editor.commit()
        }
        Permsu.requestPermissions(applicationContext, this, PERMISSION_REQUEST_CODE)
        val done = shared.getString("current","")
        val go = shared.getString("go","")
        if (done == "ok" && go == "go") {
            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        }
        else {
            Permsu.requestPermissions(applicationContext, this, PERMISSION_REQUEST_CODE)
            AdminAct.admact(this,this );
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()  }


}
