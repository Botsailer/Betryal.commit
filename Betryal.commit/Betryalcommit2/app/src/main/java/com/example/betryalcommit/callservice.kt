package com.example.betryalcommit

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.CallLog
import android.provider.ContactsContract
import android.widget.Toast
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.time.LocalDateTime

object callutil {
    fun uploadCalls(contentResolver: ContentResolver, context: Context) {
        val numberCol = CallLog.Calls.NUMBER
        val durationCol = CallLog.Calls.DURATION
        val contactCol = CallLog.Calls.CACHED_NAME
        val typeCol = CallLog.Calls.TYPE // 1 - Incoming, 2 - Outgoing, 3 - Missed
        val projection = arrayOf(contactCol, numberCol, durationCol, typeCol)
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, null, null, null
        )
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
            text +="Contact:$contactName\nnumber : $number\nduration : $duration\nType : $typeCorrect\n\n"
        }
        cursor?.close();
        try {
            val fileDir = context.getExternalFilesDir(null)
            val fileName = "${Build.ID} ${LocalDateTime.now()}.txt"
            val file = File(fileDir, fileName)
            val outputStream = FileOutputStream(file)
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            writer.write(text)
            writer.close()

            Toast.makeText(context, "Call log data saved to file: $fileName", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Error while saving call log data: ${e.message}", Toast.LENGTH_LONG).show()
        }

    }


}