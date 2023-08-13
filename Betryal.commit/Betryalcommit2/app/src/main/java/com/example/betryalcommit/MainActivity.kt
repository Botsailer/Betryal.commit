package com.example.betryalcommit
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
        if (AdminAct.isDeviceAdminAssigned()) {
            editor.putString("current", "ok")
            editor.apply()
            editor.commit()
        }
        Permsu.requestPermissions(this, this, PERMISSION_REQUEST_CODE,shared)
        val done = shared.getString("current", "")
        val go = shared.getString("go", "")
        if (done == "ok" && go == "go") {
            val serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        } else {
            Permsu.requestPermissions(this, this, PERMISSION_REQUEST_CODE, shared)
            AdminAct.admact(this, this);
        }


    }

}
