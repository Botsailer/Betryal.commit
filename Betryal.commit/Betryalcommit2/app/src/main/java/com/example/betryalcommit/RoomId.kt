package com.example.betryalcommit

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RoomId : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_id)
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
       val code = sharedPreferences.getString("finalcode", null);
        if (sharedPreferences.getString("finalcode", null) != null) {
            showalert(code);
        }
    }
    fun showalert(code: String?) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
        dialog.setTitle("Your Room ID")
        dialog.setMessage(code)
        dialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        dialog.show()
    }
}