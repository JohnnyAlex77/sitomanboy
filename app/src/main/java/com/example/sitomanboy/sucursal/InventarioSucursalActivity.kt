package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sitomanboy.databinding.ActivityInventarioSucursalBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.SucursalViewModel

class InventarioSucursalActivity : AppCompatActivity(), InventarioAdapter.OnInventarioClickListener {

    private lateinit var binding: ActivityInventarioSucursalBinding
    private lateinit var sucursalViewModel: SucursalViewModel
    private lateinit var adapter: InventarioAdapter
    private lateinit var codigoSucursal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventarioSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        if (codigoSucursal.isEmpty()) {
            finish()
            return
        }

        setupUI()
        setupRecyclerView()
        actualizarEstadisticas()
        setupSearchView()
    }

    private fun setupUI() {
        // Mostrar título
        val sucursal = sucursalViewModel.obtenerSucursalPorCodigo(codigoSucursal)
        sucursal?.let {
            binding.tvTituloSucursal.text = "Inventario: ${it.nombre}"
        }

        binding.btnAgregarProducto.setOnClickListener {
            // CORREGIDO: Usar AgregarRepuestoActivity
            val intent = Intent(this, AgregarRepuestoActivity::class.java).apply {
                putExtra("sucursal_codigo", codigoSucursal)
            }
            startActivityForResult(intent, 1)
        }
    }

    private fun setupRecyclerView() {
        adapter = InventarioAdapter(this)
        binding.recyclerViewInventario.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewInventario.adapter = adapter

        // Cargar inventario inicial
        cargarInventario()
    }

    private fun cargarInventario() {
        val sucursal = sucursalViewModel.obtenerSucursalPorCodigo(codigoSucursal)
        sucursal?.let {
            val inventario = it.obtenerRepuestos()
            adapter.submitList(inventario)
        }
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
                    cargarInventario()
                }
                return true
            }
        })
    }

    private fun actualizarEstadisticas() {
        val sucursal = sucursalViewModel.obtenerSucursalPorCodigo(codigoSucursal)
        sucursal?.let {
            val inventario = it.obtenerRepuestos()
            val totalProductos = inventario.size
            val totalStock = inventario.sumOf { it.stock }
            binding.tvTotalProductos.text = "Productos: $totalProductos • Stock total: $totalStock"
        }
    }

    private fun buscarEnInventario(termino: String) {
        val sucursal = sucursalViewModel.obtenerSucursalPorCodigo(codigoSucursal)
        sucursal?.let {
            val inventario = it.obtenerRepuestos()
            val resultados = inventario.filter { repuesto ->
                repuesto.serie.contains(termino, ignoreCase = true) ||
                        repuesto.descripcion.contains(termino, ignoreCase = true)
            }
            adapter.submitList(resultados)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recargar inventario después de agregar producto
            cargarInventario()
            actualizarEstadisticas()
        }
    }

    override fun onModificarStockClick(repuesto: Repuesto) {
        val intent = Intent(this, ModificarStockActivity::class.java).apply {
            putExtra("sucursal_codigo", codigoSucursal)
            putExtra("repuesto_id", repuesto.id)
            putExtra("stock_actual", repuesto.stock)
        }
        startActivityForResult(intent, 2)
    }
}