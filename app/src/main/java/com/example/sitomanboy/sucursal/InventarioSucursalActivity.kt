package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.databinding.ActivityInventarioSucursalBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.SucursalViewModel

class InventarioSucursalActivity : AppCompatActivity(), InventarioAdapter.OnInventarioClickListener {

    private lateinit var binding: ActivityInventarioSucursalBinding
    private lateinit var sucursalViewModel: SucursalViewModel
    private lateinit var adapter: InventarioAdapter
    private lateinit var sucursal: Sucursal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventarioSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        val codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        cargarSucursal(codigoSucursal)

        setupUI()
        setupRecyclerView()
        actualizarEstadisticas()
        setupSearchView()
    }

    private fun cargarSucursal(codigo: String) {
        val sucursalCargada = sucursalViewModel.obtenerSucursalPorCodigo(codigo)
        sucursalCargada?.let {
            sucursal = it
            binding.tvTituloSucursal.text = "Inventario: ${it.nombre}"
        }
    }

    private fun setupUI() {
        binding.btnAgregarProducto.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java).apply {
                putExtra("sucursal_codigo", sucursal.codigo)
            }
            startActivityForResult(intent, 1)
        }
    }

    private fun setupRecyclerView() {
        adapter = InventarioAdapter(this)
        binding.recyclerViewInventario.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewInventario.adapter = adapter

        // Cargar inventario inicial
        val inventario = sucursal.obtenerRepuestos()
        adapter.submitList(inventario)
    }

    private fun setupSearchView() {
        binding.searchViewInventario.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    buscarEnInventario(newText)
                } else {
                    // Mostrar todo el inventario si la búsqueda está vacía
                    val inventarioCompleto = sucursal.obtenerRepuestos()
                    adapter.submitList(inventarioCompleto)
                }
                return true
            }
        })
    }

    private fun actualizarEstadisticas() {
        val inventario = sucursal.obtenerRepuestos()
        val totalProductos = inventario.size
        binding.tvTotalProductos.text = "Productos en inventario: $totalProductos"
    }

    private fun buscarEnInventario(termino: String) {
        val inventario = sucursal.obtenerRepuestos()
        val resultados = inventario.filter {
            it.serie.contains(termino, ignoreCase = true) ||
                    it.descripcion.contains(termino, ignoreCase = true)
        }
        adapter.submitList(resultados)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recargar inventario después de agregar producto
            cargarSucursal(sucursal.codigo) // Recargar sucursal
            val inventarioActual = sucursal.obtenerRepuestos()
            adapter.submitList(inventarioActual)
            actualizarEstadisticas()
        }
    }

    override fun onModificarStockClick(repuesto: Repuesto) {
        val intent = Intent(this, ModificarStockActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
            putExtra("repuesto_id", repuesto.id)
            putExtra("stock_actual", repuesto.stock)
        }
        startActivityForResult(intent, 2)
    }
}