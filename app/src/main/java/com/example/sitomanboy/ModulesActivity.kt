package com.example.sitomanboy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitomanboy.databinding.ActivityModulesBinding
import com.example.sitomanboy.repuesto.RepuestoActivity  // Importar correctamente
import com.example.sitomanboy.sucursal.SucursalActivity  // Importar correctamente

class ModulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Navegación a módulo Repuestos
        binding.btnRepuestos.setOnClickListener {
            val intent = Intent(this, RepuestoActivity::class.java)  // Usar clase directamente
            startActivity(intent)
        }

        // Navegación a módulo Sucursales
        binding.btnSucursales.setOnClickListener {
            val intent = Intent(this, SucursalActivity::class.java)  // Usar clase directamente
            startActivity(intent)
        }
    }
}