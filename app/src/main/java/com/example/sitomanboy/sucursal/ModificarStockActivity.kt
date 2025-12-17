package com.example.sitomanboy.sucursal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
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
        binding.tvStockActual.text = "Stock actual: $stockActual"
        binding.etNuevoStock.setText(stockActual.toString())

        binding.btnGuardar.setOnClickListener {
            modificarStock()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // Validación del nuevo stock
        binding.etNuevoStock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().trim()
                val esValido = try {
                    texto.toInt() >= 0
                } catch (e: NumberFormatException) {
                    false
                }

                if (esValido) {
                    binding.etNuevoStock.setBackgroundResource(R.drawable.bg_edittext_valid)
                } else {
                    binding.etNuevoStock.setBackgroundResource(R.drawable.bg_edittext_invalid)
                }
            }
        })
    }

    private fun modificarStock() {
        val nuevoStockText = binding.etNuevoStock.text.toString().trim()

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