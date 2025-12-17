package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityRepuestoBinding
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class RepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepuestoBinding
    private lateinit var viewModel: RepuestoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        binding.btnCrearRepuesto.setOnClickListener {
            val intent = Intent(this, CrearRepuestoActivity::class.java)
            startActivity(intent)
        }

        binding.btnListaRepuestos.setOnClickListener {
            val intent = Intent(this, ListaRepuestosActivity::class.java)
            startActivity(intent)
        }

        // Botón para buscar repuestos
        binding.btnBuscarRepuesto.setOnClickListener {
            val serieONombre = binding.etBuscarRepuesto.text.toString().trim()
            if (serieONombre.isNotEmpty()) {
                val intent = Intent(this, ListaRepuestosActivity::class.java).apply {
                    putExtra("busqueda", serieONombre)
                }
                startActivity(intent)
            }
        }
    }
}