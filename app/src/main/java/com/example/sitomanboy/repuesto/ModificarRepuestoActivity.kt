package com.example.sitomanboy.repuesto

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityModificarRepuestoBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class ModificarRepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarRepuestoBinding
    private lateinit var viewModel: RepuestoViewModel
    private lateinit var repuestoOriginal: Repuesto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]
        repuestoOriginal = intent.getSerializableExtra("repuesto") as Repuesto

        cargarDatosRepuesto()
        setupUI()
    }

    private fun cargarDatosRepuesto() {
        binding.tvId.text = repuestoOriginal.id.toString()
        binding.etSerie.setText(repuestoOriginal.serie)
        binding.etDescripcion.setText(repuestoOriginal.descripcion)
        binding.etStock.setText(repuestoOriginal.stock.toString())
    }

    private fun setupUI() {
        binding.btnActualizar.setOnClickListener {
            guardarCambios()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarCambios() {
        val serie = binding.etSerie.text.toString().trim()
        val descripcion = binding.etDescripcion.text.toString().trim()
        val stock = try {
            binding.etStock.text.toString().trim().toInt()
        } catch (e: NumberFormatException) {
            0
        }

        // Validaciones básicas
        if (serie.isEmpty() || descripcion.isEmpty() || stock < 0) {
            Toast.makeText(this, "Por favor complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val repuestoActualizado = repuestoOriginal.copy(
            serie = serie,
            descripcion = descripcion,
            stock = stock
        )

        val exito = viewModel.actualizarRepuesto(repuestoActualizado)

        if (exito) {
            Toast.makeText(this, "Repuesto actualizado exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar repuesto", Toast.LENGTH_SHORT).show()
        }
    }
}