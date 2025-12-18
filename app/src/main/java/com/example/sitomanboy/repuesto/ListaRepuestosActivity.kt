package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.databinding.ActivityListaRepuestosBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class ListaRepuestosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaRepuestosBinding
    private lateinit var viewModel: RepuestoViewModel
    private lateinit var adapter: RepuestoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaRepuestosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        setupRecyclerView()
        observarRepuestos()

        // Verificar si hay búsqueda
        val busqueda = intent.getStringExtra("busqueda")
        if (!busqueda.isNullOrEmpty()) {
            realizarBusqueda(busqueda)
        }
    }

    private fun setupRecyclerView() {
        adapter = RepuestoAdapter(object : RepuestoAdapter.OnRepuestoClickListener {
            override fun onVerClick(repuesto: Repuesto) {
                val intent = Intent(this@ListaRepuestosActivity, DetalleRepuestoActivity::class.java).apply {
                    putExtra("repuesto", repuesto as android.os.Parcelable)  // Cast explícito
                }
                startActivity(intent)
            }

            override fun onModificarClick(repuesto: Repuesto) {
                val intent = Intent(this@ListaRepuestosActivity, ModificarRepuestoActivity::class.java).apply {
                    putExtra("repuesto", repuesto as android.os.Parcelable)  // Cast explícito
                }
                startActivity(intent)
            }

            override fun onEliminarClick(repuesto: Repuesto) {
                val intent = Intent(this@ListaRepuestosActivity, ConfirmarEliminacionActivity::class.java).apply {
                    putExtra("repuesto", repuesto as android.os.Parcelable)  // Cast explícito
                    putExtra("tipo", "repuesto")
                }
                startActivity(intent)
            }
        })

        binding.recyclerViewListaRepuestos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewListaRepuestos.adapter = adapter
    }

    private fun observarRepuestos() {
        viewModel.repuestos.observe(this, Observer { repuestos ->
            adapter.submitList(repuestos)
        })
    }

    private fun realizarBusqueda(termino: String) {
        val resultados = viewModel.buscarRepuestos(termino)
        adapter.submitList(resultados)
    }
}