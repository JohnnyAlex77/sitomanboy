package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityConfirmarEliminacionBinding
import com.example.sitomanboy.viewmodel.RepuestoViewModel
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ConfirmarEliminacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEliminacionBinding
    private lateinit var repuestoViewModel: RepuestoViewModel
    private lateinit var sucursalViewModel: SucursalViewModel
    private var repuestoId: Int = 0
    private var tipo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEliminacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repuestoViewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]
        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        repuestoId = intent.getIntExtra("repuesto_id", 0)
        tipo = intent.getStringExtra("tipo") ?: ""

        setupUI()
    }

    private fun setupUI() {
        binding.tvMensaje.text = "¿Estás seguro de que deseas eliminar este $tipo?"

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnConfirmar.setOnClickListener {
            eliminarElemento()
        }
    }

    private fun eliminarElemento() {
        when (tipo) {
            "repuesto" -> {
                val exito = repuestoViewModel.eliminarRepuesto(repuestoId)
                if (exito) {
                    Toast.makeText(this, "Repuesto eliminado exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al eliminar repuesto", Toast.LENGTH_SHORT).show()
                }
            }
            // Puedes agregar más casos aquí para otros tipos
        }

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}