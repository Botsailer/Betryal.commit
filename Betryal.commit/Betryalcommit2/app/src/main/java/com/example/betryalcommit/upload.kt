package com.example.betryalcommit
import okhttp3.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class Uplink(private val serverUrl: String) {

    private val client = OkHttpClient()

    fun push(file: File) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file))
            .build()

        val request = Request.Builder()
            .url("http://192.168.31.50:8080/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        println("Upload failed: ${response.code} - ${response.message}")
                    } else {
                        println("Upload successful")
                    }
                }
            }
        })
    }
}