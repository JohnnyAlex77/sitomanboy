package com.example.sitomanboy.repuesto

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityCrearRepuestoBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class CrearRepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearRepuestoBinding
    private lateinit var viewModel: RepuestoViewModel
    private var serieValida = false
    private var descripcionValida = false
    private var stockValido = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        setupUI()
        setupValidaciones()
    }

    private fun setupUI() {
        binding.btnCrear.isVisible = false

        binding.btnCrear.setOnClickListener {
            crearRepuesto()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun setupValidaciones() {
        // Validación de serie
        binding.etSerie.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                serieValida = s.toString().trim().isNotEmpty()
                actualizarColorCampo(binding.etSerie, serieValida)
                verificarCampos()
            }
        })

        // Validación de descripción
        binding.etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                descripcionValida = s.toString().trim().isNotEmpty()
                actualizarColorCampo(binding.etDescripcion, descripcionValida)
                verificarCampos()
            }
        })

        // Validación de stock
        binding.etStock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().trim()
                stockValido = try {
                    texto.toInt() >= 0
                } catch (e: NumberFormatException) {
                    false
                }
                actualizarColorCampo(binding.etStock, stockValido)
                verificarCampos()
            }
        })
    }

    private fun actualizarColorCampo(campo: android.widget.EditText, esValido: Boolean) {
        if (esValido) {
            campo.setBackgroundResource(R.drawable.bg_edittext_valid)
        } else {
            campo.setBackgroundResource(R.drawable.bg_edittext_invalid)
        }
    }

    private fun verificarCampos() {
        binding.btnCrear.isVisible = serieValida && descripcionValida && stockValido
    }

    private fun crearRepuesto() {
        val serie = binding.etSerie.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val stock = try {
            binding.etStock.text.toString().trim().toInt()
        } catch (e: NumberFormatException) {
            0
        }

        val repuesto = Repuesto(
            serie = serie,
            descripcion = descripcion,
            stock = stock
        )

        viewModel.agregarRepuesto(repuesto)

        Toast.makeText(this, "Repuesto creado exitosamente", Toast.LENGTH_SHORT).show()
        finish()
    }
}