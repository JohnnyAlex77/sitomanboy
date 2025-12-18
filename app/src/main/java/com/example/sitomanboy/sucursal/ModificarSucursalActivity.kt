package com.example.sitomanboy.sucursal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityModificarSucursalBinding
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ModificarSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarSucursalBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var sucursalOriginal: Sucursal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]
        sucursalOriginal = intent.getSerializableExtra("sucursal") as Sucursal

        cargarDatosSucursal()
        setupUI()
    }

    private fun cargarDatosSucursal() {
        binding.tvCodigo.text = sucursalOriginal.codigo
        binding.etNombre.setText(sucursalOriginal.nombre)
        binding.etDireccion.setText(sucursalOriginal.direccion)
    }

    private fun setupUI() {
        binding.btnActualizarSucursal.setOnClickListener {
            guardarCambios()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarCambios() {
        val codigo = sucursalOriginal.codigo // No cambia
        val nombre = binding.etNombre.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        // Validaciones
        if (nombre.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val sucursalActualizada = sucursalOriginal.copy(
            nombre = nombre,
            direccion = direccion
        )

        val exito = viewModel.actualizarSucursal(sucursalActualizada)

        if (exito) {
            Toast.makeText(this, "Sucursal actualizada exitosamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar sucursal", Toast.LENGTH_SHORT).show()
        }
    }
}