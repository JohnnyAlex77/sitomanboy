package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityDetalleSucursalBinding
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class DetalleSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleSucursalBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var sucursal: Sucursal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        val codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        cargarSucursal(codigoSucursal)

        setupUI()
    }

    private fun setupUI() {
        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.btnVerInventario.setOnClickListener {
            val intent = Intent(this, InventarioSucursalActivity::class.java).apply {
                putExtra("sucursal_codigo", sucursal.codigo)
            }
            startActivity(intent)
        }

        binding.btnModificar.setOnClickListener {
            val intent = Intent(this, ModificarSucursalActivity::class.java).apply {
                putExtra("sucursal", sucursal)
            }
            startActivity(intent)
        }
    }

    private fun cargarSucursal(codigo: String) {
        val sucursalCargada = viewModel.obtenerSucursalPorCodigo(codigo)
        sucursalCargada?.let {
            sucursal = it
            binding.tvCodigo.text = it.codigo
            binding.tvNombre.text = it.nombre
            binding.tvDireccion.text = it.direccion

            // Calcular estadísticas
            val repuestos = it.obtenerRepuestos()
            val totalRepuestos = repuestos.size
            val totalStock = repuestos.sumOf { repuesto -> repuesto.stock }

            binding.tvTotalRepuestos.text = "Repuestos en inventario: $totalRepuestos"
            binding.tvTotalStock.text = "Stock total: $totalStock unidades"
        }
    }
}