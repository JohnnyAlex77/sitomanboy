package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityListaSucursalesBinding
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.repuesto.RepuestoActivity
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ListaSucursalesActivity : AppCompatActivity(), SucursalAdapter.OnSucursalClickListener {

    private lateinit var binding: ActivityListaSucursalesBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var adapter: SucursalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaSucursalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        setupUI()
        setupRecyclerView()
        observarSucursales()

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

        binding.btnNuevaSucursal.setOnClickListener {
            val intent = Intent(this, CrearSucursalActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = SucursalAdapter(this)
        binding.rvSucursales.layoutManager = LinearLayoutManager(this)
        binding.rvSucursales.adapter = adapter
    }

    private fun observarSucursales() {
        viewModel.sucursales.observe(this, Observer { sucursales ->
            adapter.submitList(sucursales)

            // Actualizar contador
            binding.tvContador.text = "Total: ${sucursales.size} sucursales"
        })
    }

    private fun realizarBusqueda(termino: String) {
        val sucursales = viewModel.sucursales.value ?: emptyList()
        val resultados = sucursales.filter {
            it.codigo.contains(termino, ignoreCase = true) ||
                    it.nombre.contains(termino, ignoreCase = true) ||
                    it.direccion.contains(termino, ignoreCase = true)
        }
        adapter.submitList(resultados)
        binding.tvTitulo.text = "Resultados de búsqueda: $termino"
    }

    override fun onVerClick(sucursal: Sucursal) {
        val intent = Intent(this, DetalleSucursalActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
        }
        startActivity(intent)
    }

    override fun onModificarClick(sucursal: Sucursal) {
        val intent = Intent(this, ModificarSucursalActivity::class.java).apply {
            putExtra("sucursal", sucursal)
        }
        startActivity(intent)
    }

    override fun onEliminarClick(sucursal: Sucursal) {
        val intent = Intent(this, ConfirmarEliminacionSucursalActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
            putExtra("tipo", "sucursal")
        }
        startActivityForResult(intent, 1)
    }

    override fun onInventarioClick(sucursal: Sucursal) {
        val intent = Intent(this, InventarioSucursalActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
        }
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recargar lista después de eliminar
            val listaActual = viewModel.sucursales.value ?: mutableListOf()
            adapter.submitList(listaActual)
        }
    }
}