package com.example.sitomanboy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitomanboy.databinding.ActivityModulesBinding
import com.example.sitomanboy.repuesto.RepuestoActivity
import com.example.sitomanboy.sucursal.SucursalActivity
import com.example.sitomanboy.repuesto.CrearRepuestoActivity
import com.example.sitomanboy.sucursal.CrearSucursalActivity

class ModulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        // Configurar los 4 botones principales requeridos

        // 1. Botón para ver repuestos
        binding.btnRepuestos.setOnClickListener {
            val intent = Intent(this, RepuestoActivity::class.java)
            startActivity(intent)
        }

        // 2. Botón para ver sucursales
        binding.btnSucursales.setOnClickListener {
            val intent = Intent(this, SucursalActivity::class.java)
            startActivity(intent)
        }

        // 3. Botón para crear repuesto
        binding.btnCrearRepuesto.setOnClickListener {
            val intent = Intent(this, CrearRepuestoActivity::class.java)
            startActivity(intent)
        }

        // 4. Botón para crear sucursal
        binding.btnCrearSucursal.setOnClickListener {
            val intent = Intent(this, CrearSucursalActivity::class.java)
            startActivity(intent)
        }

        // Botón adicional para buscar stock (opcional)
        binding.btnBuscarStock.setOnClickListener {
            // Para cumplir con el requisito de "Buscar stock de repuestos"
            // Puedes crear una actividad específica o usar la existente
            val intent = Intent(this, RepuestoActivity::class.java)
            startActivity(intent)
        }
    }
}