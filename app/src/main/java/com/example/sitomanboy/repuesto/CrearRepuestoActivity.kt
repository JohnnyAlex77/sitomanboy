package com.example.sitomanboy.repuesto

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
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
        binding.btnConfirmar.setOnClickListener {
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
                actualizarEstadoCampos()
                verificarCampos()
            }
        })

        // Validación de descripción
        binding.etDescripcion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                descripcionValida = s.toString().trim().isNotEmpty()
                actualizarEstadoCampos()
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
                    texto.isNotEmpty() && texto.toInt() >= 0
                } catch (e: NumberFormatException) {
                    false
                }
                actualizarEstadoCampos()
                verificarCampos()
            }
        })
    }

    private fun actualizarEstadoCampos() {
        // Esta función maneja el estado visual de los campos
        if (serieValida) {
            binding.etSerie.background = getDrawable(R.drawable.bg_edittext_valid)
        } else {
            binding.etSerie.background = getDrawable(R.drawable.bg_edittext)
        }

        if (descripcionValida) {
            binding.etDescripcion.background = getDrawable(R.drawable.bg_edittext_valid)
        } else {
            binding.etDescripcion.background = getDrawable(R.drawable.bg_edittext)
        }

        if (stockValido) {
            binding.etStock.background = getDrawable(R.drawable.bg_edittext_valid)
        } else {
            binding.etStock.background = getDrawable(R.drawable.bg_edittext)
        }
    }

    private fun verificarCampos() {
        binding.btnConfirmar.isEnabled = serieValida && descripcionValida && stockValido
    }

    private fun crearRepuesto() {
        val serie = binding.etSerie.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val stock = try {
            binding.etStock.text.toString().trim().toInt()
        } catch (e: NumberFormatException) {
            0
        }

        // Validaciones finales
        if (!serieValida || !descripcionValida || !stockValido) {
            Toast.makeText(
                this,
                "Por favor complete todos los campos correctamente",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Crear repuesto
        val repuesto = Repuesto(
            id = 0, // ID temporal
            serie = serie,
            descripcion = descripcion,
            stock = stock
        )

        // Agregar repuesto usando ViewModel
        val exito = viewModel.agregarRepuesto(repuesto)

        if (exito) {
            Toast.makeText(this, "Repuesto creado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al crear repuesto", Toast.LENGTH_SHORT).show()
        }
    }
}