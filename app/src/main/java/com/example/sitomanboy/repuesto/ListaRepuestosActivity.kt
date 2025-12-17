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

class ListaRepuestosActivity : AppCompatActivity(), RepuestoAdapter.OnRepuestoClickListener {

    private lateinit var binding: ActivityListaRepuestosBinding
    private lateinit var viewModel: RepuestoViewModel
    private lateinit var adapter: RepuestoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaRepuestosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        setupUI()
        setupRecyclerView()
        observarRepuestos()

        // Verificar si hay búsqueda
        val busqueda = intent.getStringExtra("busqueda")
        if (!busqueda.isNullOrEmpty()) {
            realizarBusqueda(busqueda)
        }
    }

    private fun setupUI() {
        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.btnNuevoRepuesto.setOnClickListener {
            val intent = Intent(this, CrearRepuestoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = RepuestoAdapter(this)
        binding.rvRepuestos.layoutManager = LinearLayoutManager(this)
        binding.rvRepuestos.adapter = adapter
    }

    private fun observarRepuestos() {
        viewModel.repuestos.observe(this, Observer { repuestos ->
            adapter.submitList(repuestos)
        })
    }

    private fun realizarBusqueda(termino: String) {
        val resultados = viewModel.buscarRepuestos(termino)
        adapter.submitList(resultados)
        binding.tvTitulo.text = "Resultados de búsqueda: $termino"
    }

    override fun onVerClick(repuesto: Repuesto) {
        val intent = Intent(this, DetalleRepuestoActivity::class.java).apply {
            putExtra("repuesto_id", repuesto.id)
        }
        startActivity(intent)
    }

    override fun onModificarClick(repuesto: Repuesto) {
        val intent = Intent(this, ModificarRepuestoActivity::class.java).apply {
            putExtra("repuesto", repuesto)
        }
        startActivity(intent)
    }

    override fun onEliminarClick(repuesto: Repuesto) {
        val intent = Intent(this, ConfirmarEliminacionActivity::class.java).apply {
            putExtra("repuesto_id", repuesto.id)
            putExtra("tipo", "repuesto")
        }
        startActivity(intent)
    }
}