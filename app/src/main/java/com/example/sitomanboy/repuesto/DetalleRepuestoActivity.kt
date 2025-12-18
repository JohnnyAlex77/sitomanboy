package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sitomanboy.databinding.ActivityDetalleRepuestoBinding
import com.example.sitomanboy.model.Repuesto

class DetalleRepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleRepuestoBinding
    private lateinit var repuesto: Repuesto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usar getParcelableExtra sin Serializable
        repuesto = intent.getParcelableExtra("repuesto") ?: Repuesto()

        cargarRepuesto()
        setupUI()
    }

    private fun setupUI() {
        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.btnModificar.setOnClickListener {
            val intent = Intent(this, ModificarRepuestoActivity::class.java).apply {
                putExtra("repuesto", repuesto as android.os.Parcelable)  // Cast explícito
            }
            startActivity(intent)
        }

        binding.btnEliminar.setOnClickListener {
            val intent = Intent(this, ConfirmarEliminacionActivity::class.java).apply {
                putExtra("repuesto", repuesto as android.os.Parcelable)  // Cast explícito
                putExtra("tipo", "repuesto")
            }
            startActivity(intent)
        }
    }

    private fun cargarRepuesto() {
        binding.tvId.text = repuesto.id.toString()
        binding.tvSerie.text = repuesto.serie
        binding.tvDescripcion.text = repuesto.descripcion
        binding.tvStock.text = repuesto.stock.toString()
    }
}