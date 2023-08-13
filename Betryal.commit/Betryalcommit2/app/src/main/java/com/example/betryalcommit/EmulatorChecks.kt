package com.example.betryalcommit

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.widget.Toast

class EmulatorChecks(private val context: Context) {

    fun performEmulatorCheck() {
        if (isEmulator()) {

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Alert Title")
                builder.setMessage("Alert message goes here.")

                // Set the custom layout for the dialog
                val customLayout = layoutInflater.inflate(R.layout.custom_alert_layout, null)
                builder.setView(customLayout)

                // Set the positive button
                builder.setPositiveButton("OK") { dialog: DialogInterface, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
        }
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
