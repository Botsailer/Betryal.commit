package com.example.betryalcommit

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import java.util.Random

class sucessscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucessscreen)
        val name = findViewById<EditText>(R.id.devname)

        val dis = findViewById<TextView>(R.id.code2);
        val code = findViewById<TextView>(R.id.code);
        val btn = findViewById<TextView>(R.id.genbtn);
        val continuez = findViewById<TextView>(R.id.buttoncon);

        btn.setOnClickListener {
            if (name.toString().isEmpty()) {
                name.error = "Please enter your name"
            } else {
                val finalcode = name.text.toString() + generateCode()
                code.setText(finalcode);
                dis.visibility = TextView.VISIBLE;
                Log.d("finalcode", finalcode);
            }
        }

        val btn2 = findViewById<TextView>(R.id.reset)
        btn2.setOnClickListener {
            name.setText("");
            code.text = "";
        }
        continuez.setOnClickListener {
            val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("finalcode", code.text.toString());
            editor.apply()
            editor.commit();
            finish()
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent)
        }




    }

    fun generateCode(): String {
        val random = Random()
        val otp = random.nextInt(900000) + 100000
        return otp.toString()

    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    return ;
    }

}