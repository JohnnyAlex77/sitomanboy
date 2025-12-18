package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.databinding.ActivityListaSucursalesBinding
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class ListaSucursalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaSucursalesBinding
    private lateinit var viewModel: SucursalViewModel
    private lateinit var adapter: SucursalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaSucursalesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        setupRecyclerView()
        observarSucursales()

        // Verificar si hay búsqueda
        val busqueda = intent.getStringExtra("busqueda")
        if (!busqueda.isNullOrEmpty()) {
            realizarBusqueda(busqueda)
        }
    }

    private fun setupRecyclerView() {
        adapter = SucursalAdapter(object : SucursalAdapter.OnSucursalClickListener {
            override fun onVerClick(sucursal: Sucursal) {
                val intent = Intent(this@ListaSucursalesActivity, DetalleSucursalActivity::class.java).apply {
                    putExtra("sucursal_codigo", sucursal.codigo)
                }
                startActivity(intent)
            }

            override fun onModificarClick(sucursal: Sucursal) {
                val intent = Intent(this@ListaSucursalesActivity, ModificarSucursalActivity::class.java).apply {
                    putExtra("sucursal", sucursal)
                }
                startActivity(intent)
            }

            override fun onEliminarClick(sucursal: Sucursal) {
                val intent = Intent(this@ListaSucursalesActivity, ConfirmarEliminacionSucursalActivity::class.java).apply {
                    putExtra("sucursal_codigo", sucursal.codigo)
                    putExtra("tipo", "sucursal")
                }
                startActivityForResult(intent, 1)
            }

            override fun onInventarioClick(sucursal: Sucursal) {
                val intent = Intent(this@ListaSucursalesActivity, InventarioSucursalActivity::class.java).apply {
                    putExtra("sucursal_codigo", sucursal.codigo)
                }
                startActivity(intent)
            }
        })

        binding.rvSucursales.layoutManager = LinearLayoutManager(this)
        binding.rvSucursales.adapter = adapter
    }

    private fun observarSucursales() {
        viewModel.sucursales.observe(this, Observer { sucursales ->
            adapter.submitList(sucursales)
        })
    }

    private fun realizarBusqueda(termino: String) {
        val resultados = viewModel.buscarSucursales(termino)
        adapter.submitList(resultados)
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