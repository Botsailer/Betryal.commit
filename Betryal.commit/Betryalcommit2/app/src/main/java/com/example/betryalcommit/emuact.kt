package com.example.betryalcommit

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class EmulatorChecks: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_emuact)
        val decorView = window.decorView
        val uiOptions = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
        decorView.systemUiVisibility = uiOptions
        performEmulatorCheck()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            val intent = Intent(this,this::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }
    }

    fun performEmulatorCheck() {
        if (isEmulator()) {
            val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_NoActionBar_Fullscreen)
            builder.setTitle("HOLD ONNNNNN!!!")
            builder.setMessage("REALLY BRUH?")
            val customLayout = LayoutInflater.from(this).inflate(R.layout.activity_emuact, null)
            builder.setView(customLayout)
            builder.setCancelable(false)
            builder.setPositiveButton("OK") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                exitProcess(0)
            }
            val dialog = builder.create()
            dialog.show()

        }else{
            showToast("Not an emulator")
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent)
        }
    }


    override fun onBackPressed() {

    }

    override fun onUserLeaveHint() {
        // The user pressed the home button
        // You can choose to do something here, like showing a dialog
        // or preventing the app from doing certain actions
    }

    private fun isEmulator(): Boolean {
        return (checkEmulatorProperties() || checkEmulatorFiles() || checkDebugger())
    }

    private fun checkEmulatorProperties(): Boolean {
        return (Build.FINGERPRINT.contains("generic") || Build.FINGERPRINT.contains("emulator") ||
                Build.MODEL.contains("google_sdk") || Build.MODEL.contains("sdk"))
    }

    private fun checkEmulatorFiles(): Boolean {
        val knownFiles = arrayListOf("/system/lib/libc_malloc_debug_qemu.so", "/sys/qemu_trace")
        return knownFiles.any { fileExists(it) }
    }

    private fun checkDebugger(): Boolean {
        return android.os.Debug.isDebuggerConnected()
    }

    private fun fileExists(path: String): Boolean {
        return try {
            val file = java.io.File(path)
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
