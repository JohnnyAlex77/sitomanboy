package com.example.sitomanboy.sucursal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityCrearSucursalBinding  // Asegúrate que el layout se llama así
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class CrearSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearSucursalBinding
    private lateinit var viewModel: SucursalViewModel
    private var codigoValido = false
    private var nombreValido = false
    private var direccionValida = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        setupUI()
        setupValidaciones()
    }

    private fun setupUI() {
        binding.btnGuardarSucursal.isVisible = false

        binding.btnGuardarSucursal.setOnClickListener {
            crearSucursal()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun setupValidaciones() {
        // Validación de código
        binding.etCodigo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                codigoValido = s.toString().trim().isNotEmpty()
                verificarCampos()
            }
        })

        // Validación de nombre
        binding.etNombre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                nombreValido = s.toString().trim().isNotEmpty()
                verificarCampos()
            }
        })

        // Validación de dirección
        binding.etDireccion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                direccionValida = s.toString().trim().isNotEmpty()
                verificarCampos()
            }
        })
    }

    private fun verificarCampos() {
        binding.btnGuardarSucursal.isVisible = codigoValido && nombreValido && direccionValida
    }

    private fun crearSucursal() {
        val codigo = binding.etCodigo.text.toString().trim()
        val nombre = binding.etNombre.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        // Verificar si el código ya existe
        val sucursalExistente = viewModel.obtenerSucursalPorCodigo(codigo)
        if (sucursalExistente != null) {
            Toast.makeText(this, "Ya existe una sucursal con este código", Toast.LENGTH_SHORT).show()
            return
        }

        val sucursal = Sucursal(
            codigo = codigo,
            nombre = nombre,
            direccion = direccion
        )

        // El método agregarSucursal no retorna valor
        viewModel.agregarSucursal(sucursal)

        Toast.makeText(this, "Sucursal creada exitosamente", Toast.LENGTH_SHORT).show()
        finish()
    }
}