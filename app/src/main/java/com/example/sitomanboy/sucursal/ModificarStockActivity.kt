package com.example.sitomanboy.sucursal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityModificarStockBinding
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ModificarStockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarStockBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var codigoSucursal: String
    private var repuestoId: Int = 0
    private var stockActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        repuestoId = intent.getIntExtra("repuesto_id", 0)
        stockActual = intent.getIntExtra("stock_actual", 0)

        setupUI()
    }

    private fun setupUI() {
        // Mostrar stock actual en un TextView si existe
        try {
            binding.tvStockActual.text = "Stock actual: $stockActual"
        } catch (e: Exception) {
            // Si el TextView no existe en el layout, no mostrar nada
        }

        binding.etStock.setText(stockActual.toString())

        binding.btnActualizar.setOnClickListener {
            modificarStock()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun modificarStock() {
        val nuevoStockText = binding.etStock.text.toString().trim()

        if (nuevoStockText.isEmpty()) {
            Toast.makeText(this, "Ingrese el nuevo stock", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoStock = try {
            nuevoStockText.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Stock debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevoStock < 0) {
            Toast.makeText(this, "Stock no puede ser negativo", Toast.LENGTH_SHORT).show()
            return
        }

        val exito = viewModel.actualizarStockEnSucursal(codigoSucursal, repuestoId, nuevoStock)

        if (exito) {
            Toast.makeText(this, "Stock actualizado exitosamente", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar stock", Toast.LENGTH_SHORT).show()
        }
    }
}