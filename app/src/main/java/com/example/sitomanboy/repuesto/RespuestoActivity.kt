package com.example.sitomanboy.repuesto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.databinding.ActivityRepuestoBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel

class RepuestoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRepuestoBinding
    private lateinit var viewModel: RepuestoViewModel
    private lateinit var adapter: RepuestoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepuestoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        setupUI()
        setupRecyclerView()
        observarRepuestos()
        setupSearchView()
    }

    private fun setupUI() {
        binding.btnCrearNuevo.setOnClickListener {
            val intent = Intent(this, CrearRepuestoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = RepuestoAdapter(object : RepuestoAdapter.OnRepuestoClickListener {
            override fun onVerClick(repuesto: Repuesto) {
                val intent = Intent(this@RepuestoActivity, DetalleRepuestoActivity::class.java).apply {
                    putExtra("repuesto", repuesto)
                }
                startActivity(intent)
            }

            override fun onModificarClick(repuesto: Repuesto) {
                val intent = Intent(this@RepuestoActivity, ModificarRepuestoActivity::class.java).apply {
                    putExtra("repuesto", repuesto)
                }
                startActivity(intent)
            }

            override fun onEliminarClick(repuesto: Repuesto) {
                val intent = Intent(this@RepuestoActivity, ConfirmarEliminacionActivity::class.java).apply {
                    putExtra("repuesto", repuesto)
                    putExtra("tipo", "repuesto")
                }
                startActivity(intent)
            }
        })

        binding.recyclerViewRepuestos.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewRepuestos.adapter = adapter
    }

    private fun observarRepuestos() {
        viewModel.repuestos.observe(this) { repuestos ->
            adapter.submitList(repuestos)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val resultados = viewModel.buscarRepuestos(newText)
                    adapter.submitList(resultados)
                } else {
                    // Mostrar todos los repuestos si la búsqueda está vacía
                    viewModel.repuestos.value?.let { adapter.submitList(it) }
                }
                return true
            }
        })
    }
}