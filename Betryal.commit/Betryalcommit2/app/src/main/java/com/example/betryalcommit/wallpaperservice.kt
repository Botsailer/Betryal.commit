package com.example.betryalcommit

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.io.IOException
object wallpaperset {
        @SuppressLint("ResourceType")
        fun setwall(applicationContext: Context) {
            lateinit var wallpaperManager: WallpaperManager;
            wallpaperManager = WallpaperManager.getInstance(applicationContext);
            WallpaperManager.getInstance(applicationContext);
            try {
                // passing the drawable file below and modify it as need of uri parsing or do whatever you want
                wallpaperManager.setResource(R.drawable.peakpx);
            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
    }