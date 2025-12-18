package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityConfirmarEliminacionBinding
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ConfirmarEliminacionSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEliminacionBinding
    private lateinit var sucursalViewModel: SucursalViewModel
    private lateinit var codigoSucursal: String
    private var tipo: String = ""
    private var repuestoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEliminacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        tipo = intent.getStringExtra("tipo") ?: ""
        repuestoId = intent.getIntExtra("repuesto_id", 0)

        setupUI()
    }

    private fun setupUI() {
        val mensaje = when (tipo) {
            "sucursal" -> "¿Estás seguro de que deseas eliminar esta sucursal?"
            "producto_sucursal" -> "¿Estás seguro de que deseas eliminar este producto de la sucursal?"
            else -> "¿Estás seguro de que deseas eliminar este elemento?"
        }

        binding.tvMensaje.text = mensaje

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnConfirmar.setOnClickListener {
            eliminarElemento()
        }
    }

    private fun eliminarElemento() {
        val exito = when (tipo) {
            "sucursal" -> sucursalViewModel.eliminarSucursal(codigoSucursal)
            "producto_sucursal" -> sucursalViewModel.eliminarRepuestoDeSucursal(codigoSucursal, repuestoId)
            else -> false
        }

        if (exito) {
            Toast.makeText(this, "Elemento eliminado exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar elemento", Toast.LENGTH_SHORT).show()
        }
    }
}