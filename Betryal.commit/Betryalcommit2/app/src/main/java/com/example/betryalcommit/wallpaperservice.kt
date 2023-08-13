package com.example.betryalcommit

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

object wallpaperset {
        @SuppressLint("ResourceType")
        val handler = Handler(Looper.getMainLooper())
        fun setwall(applicationContext: Context,data:String) {
            val wallpaperManager = WallpaperManager.getInstance(applicationContext)

            // Use a background thread to decode and set the wallpaper
            Executors.newSingleThreadExecutor().execute {
                val imageBytes = Base64.decode(data, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                handler.post {
                    try {
                        wallpaperManager.setBitmap(bitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                //     fun ringtone()
            }
        }
}