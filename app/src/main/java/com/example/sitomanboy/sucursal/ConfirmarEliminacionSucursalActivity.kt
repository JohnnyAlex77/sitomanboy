package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityConfirmarEliminacionSucursalBinding
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ConfirmarEliminacionSucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEliminacionSucursalBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var codigoSucursal: String
    private var tipo: String = ""
    private var repuestoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEliminacionSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        tipo = intent.getStringExtra("tipo") ?: ""
        repuestoId = intent.getIntExtra("repuesto_id", 0)

        setupUI()
    }

    private fun setupUI() {
        val mensaje = when (tipo) {
            "sucursal" -> "¿Estás seguro de que deseas eliminar esta sucursal? Se perderán todos los datos de inventario."
            "producto_sucursal" -> "¿Estás seguro de que deseas eliminar este producto del inventario de la sucursal?"
            else -> "¿Estás seguro de que deseas eliminar este elemento?"
        }

        binding.tvMensaje.text = mensaje

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnEliminar.setOnClickListener {
            eliminarElemento()
        }
    }

    private fun eliminarElemento() {
        when (tipo) {
            "sucursal" -> {
                val exito = viewModel.eliminarSucursal(codigoSucursal)
                if (exito) {
                    Toast.makeText(this, "Sucursal eliminada exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar sucursal", Toast.LENGTH_SHORT).show()
                }
            }

            "producto_sucursal" -> {
                val exito = viewModel.eliminarRepuestoDeSucursal(codigoSucursal, repuestoId)
                if (exito) {
                    Toast.makeText(this, "Producto eliminado del inventario", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}