package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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

        binding.btnGestionInventario.setOnClickListener {
            val intent = Intent(this, InventarioSucursalActivity::class.java).apply {
                putExtra("sucursal_codigo", sucursal.codigo)
            }
            startActivity(intent)
        }

        binding.btnModificarSucursal.setOnClickListener {
            val intent = Intent(this, ModificarSucursalActivity::class.java).apply {
                putExtra("sucursal", sucursal as android.os.Parcelable)  // Cast explícito
            }
            startActivity(intent)
        }

        binding.btnEliminarSucursal.setOnClickListener {
            val intent = Intent(this, ConfirmarEliminacionSucursalActivity::class.java).apply {
                putExtra("sucursal_codigo", sucursal.codigo)
                putExtra("tipo", "sucursal")
            }
            startActivityForResult(intent, 1)
        }
    }

    private fun cargarSucursal(codigo: String) {
        val sucursalCargada = viewModel.obtenerSucursalPorCodigo(codigo)
        if (sucursalCargada != null) {
            sucursal = sucursalCargada
            binding.tvCodigo.text = sucursalCargada.codigo
            binding.tvNombre.text = sucursalCargada.nombre
            binding.tvDireccion.text = sucursalCargada.direccion

            // Calcular estadísticas
            val repuestos = sucursalCargada.obtenerRepuestos()
            val totalRepuestos = repuestos.size
            val totalStock = repuestos.sumOf { it.stock }

            binding.tvTotalRepuestos.text = "Total repuestos: $totalRepuestos"
            binding.tvTotalStock.text = "Stock total: $totalStock"
        } else {
            Toast.makeText(this, "Sucursal no encontrada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Si se eliminó la sucursal, terminar esta actividad
            finish()
        }
    }
}