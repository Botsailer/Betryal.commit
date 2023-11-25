package com.example.betryalcommit

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class EmulatorChecks: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        setContentView(R.layout.activity_emuact)
        performEmulatorCheck()
    }


    fun performEmulatorCheck() {
        if (!isEmulator() ) {
            val builder = AlertDialog.Builder(this, android.R.style.Theme_Material_NoActionBar_Fullscreen)
            builder.setTitle("HOLD ONNNNNN!!!")
            builder.setMessage("REALLY BRUH?")
            val customLayout = LayoutInflater.from(this).inflate(R.layout.activity_emuact, null)
            builder.setView(customLayout)
            builder.setCancelable(false)
            builder.setPositiveButton("Maaaf kar de bhai!") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                exitProcess(0)
            }
            val dialog = builder.create()
            dialog.show()

        }else{
            showToast("Not an emulator")

            val ck = getSharedPreferences("MyPrefs", MODE_PRIVATE);


           if (ck.getString("finalcode", null) == null) {
               val intent = Intent(this, sucessscreen::class.java);
               startActivity(intent);}
            else{
        val intent = Intent(this, sucessscreen::class.java);
        startActivity(intent);
        }
        }
    }


    override fun onBackPressed() {

    }

    private fun isEmulator(): Boolean {
        return (checkEmulatorProperties() || checkEmulatorFiles() || checkDebugger() || checkEmulatorPackages());
    }

    private fun checkEmulatorProperties(): Boolean {
        return (Build.FINGERPRINT.contains("generic") || Build.FINGERPRINT.contains("emulator") ||
                Build.MODEL.contains("google_sdk") || Build.MODEL.contains("sdk"))
    }
    private fun checkEmulatorPackages(): Boolean {
        val emulatorPackages = arrayOf(        "com.bluestacks",
            "com.bignox.app",
            "com.genymotion",
            "com.memuplay",
            "com.androVM.vbox86p",
            "org.androidemu.nox",
            "com.microvirt.market",
            "com.koplayer.koplayer",
            "com.mumu.launcher",
            "com.kingroot.kinguser",
            "com.superusertools2018.gameboosterpro",
            "com.vphone.pad",
            "com.superusertools2018.turbo.cleaner",
            "com.qihoo.root",
            "com.vms.cmx.v3.android.lite",
            "com.ldmnq.wallr",
            "com.superusertools2018.systemrepair",
            "com.superusertools2018.cpu.cooler",
            "com.qihoo.magic",
            "com.doov.comm.tools",
            "com.kingroot.master",
            "com.xingyun.xinglolauncher",
            "com.tencent.mobileqqi",
            "com.wenzhibo.android.news",
            "com.tencent.qqgame",
            "com.github.shadowsocks",
            "com.elinkway.tvlive2",
            "org.proxydroid",
            "com.rrhapp.rrh",
            "com.cloud.funny",
            "cn.thecover.www");
        val packageManager = packageManager
        for (packageName in emulatorPackages) {
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                if (packageInfo != null) {
                    return true
                }
            } catch (e: PackageManager.NameNotFoundException) {
                // Package not found, continue checking other packages
            }
        }
        return false
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
