package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivityConfirmarEliminacionBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class ConfirmarEliminacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmarEliminacionBinding
    private lateinit var repuestoViewModel: RepuestoViewModel
    private lateinit var repuesto: Repuesto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmarEliminacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repuestoViewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]
        repuesto = intent.getSerializableExtra("repuesto") as Repuesto

        setupUI()
    }

    private fun setupUI() {
        binding.tvMensaje.text = "¿Estás seguro de que deseas eliminar el repuesto '${repuesto.serie}'?"

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.btnConfirmar.setOnClickListener {
            eliminarRepuesto()
        }
    }

    private fun eliminarRepuesto() {
        val exito = repuestoViewModel.eliminarRepuesto(repuesto.id)

        if (exito) {
            Toast.makeText(this, "Repuesto eliminado exitosamente", Toast.LENGTH_SHORT).show()

            // Enviar resultado OK a la actividad anterior
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar repuesto", Toast.LENGTH_SHORT).show()
        }
    }
}