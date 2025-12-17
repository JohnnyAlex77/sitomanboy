package com.example.sitomanboy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sitomanboy.databinding.ActivityModulesBinding
import com.example.sitomanboy.repuesto.RepuestoActivity
import com.example.sitomanboy.sucursal.SucursalActivity

class ModulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModulesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnModuloRepuestos.setOnClickListener {
            val intent = Intent(this, RepuestoActivity::class.java)
            startActivity(intent)
        }

        binding.btnModuloSucursales.setOnClickListener {
            val intent = Intent(this, SucursalActivity::class.java)
            startActivity(intent)
        }
    }
}