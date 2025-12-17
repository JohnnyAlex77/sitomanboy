package com.example.sitomanboy.repuesto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityDetalleRepuestoBinding
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class DetalleRepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleRepuestoBinding
    private lateinit var viewModel: RepuestoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        val repuestoId = intent.getIntExtra("repuesto_id", 0)
        cargarRepuesto(repuestoId)

        setupUI()
    }

    private fun setupUI() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarRepuesto(id: Int) {
        val repuesto = viewModel.obtenerRepuestoPorId(id)
        repuesto?.let {
            binding.tvSerie.text = it.serie
            binding.tvDescripcion.text = it.descripcion
            binding.tvStock.text = it.stock.toString()
            binding.tvId.text = it.id.toString()
        }
    }
}