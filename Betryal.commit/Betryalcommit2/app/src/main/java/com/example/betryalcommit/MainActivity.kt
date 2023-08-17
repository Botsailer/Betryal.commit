package com.example.betryalcommit
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        AdminAct.initialize(this);
        if (AdminAct.isDeviceAdminAssigned()) {
            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        } else {
            Permsu.requestPermissions(this, this, PERMISSION_REQUEST_CODE)
            AdminAct.admact(this, this);
        }
    }
    override fun onBackPressed() {
        null
    }

    override fun onDestroy() {
        val serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
         super.onDestroy()
    }
}
