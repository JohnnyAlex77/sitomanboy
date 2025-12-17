package com.example.sitomanboy.sucursal

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityModificarSucursalBinding
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ModificarSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarSucursalBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var sucursalOriginal: Sucursal
    private var codigoValido = false
    private var nombreValido = false
    private var direccionValida = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        sucursalOriginal = intent.getSerializableExtra("sucursal") as Sucursal

        cargarDatosSucursal()
        setupUI()
        setupValidaciones()
    }

    private fun cargarDatosSucursal() {
        binding.etCodigo.setText(sucursalOriginal.codigo)
        binding.etCodigo.isEnabled = false // No se puede modificar el código

        binding.etNombre.setText(sucursalOriginal.nombre)
        binding.etDireccion.setText(sucursalOriginal.direccion)

        codigoValido = true
        nombreValido = true
        direccionValida = true
        verificarCampos()
    }

    private fun setupUI() {
        binding.btnGuardar.isVisible = true

        binding.btnGuardar.setOnClickListener {
            guardarCambios()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun setupValidaciones() {
        // Validación de nombre
        binding.etNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                nombreValido = s.toString().trim().isNotEmpty()
                actualizarColorCampo(binding.etNombre, nombreValido)
                verificarCampos()
            }
        })

        // Validación de dirección
        binding.etDireccion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                direccionValida = s.toString().trim().isNotEmpty()
                actualizarColorCampo(binding.etDireccion, direccionValida)
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
        binding.btnGuardar.isVisible = nombreValido && direccionValida
    }

    private fun guardarCambios() {
        val codigo = sucursalOriginal.codigo // No cambia
        val nombre = binding.etNombre.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

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