package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityInventarioSucursalBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.model.Sucursal
import com.example.sitomanboy.viewmodel.RepuestoViewModel
import com.example.sitomanboy.viewmodel.SucursalViewModel

class InventarioSucursalActivity : AppCompatActivity(), InventarioAdapter.OnInventarioClickListener {

    private lateinit var binding: ActivityInventarioSucursalBinding
    private lateinit var sucursalViewModel: SucursalViewModel
    private lateinit var repuestoViewModel: RepuestoViewModel
    private lateinit var adapter: InventarioAdapter
    private lateinit var sucursal: Sucursal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventarioSucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]
        repuestoViewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        val codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""
        cargarSucursal(codigoSucursal)

        setupUI()
        setupRecyclerView()
        actualizarEstadisticas()
    }

    private fun cargarSucursal(codigo: String) {
        val sucursalCargada = sucursalViewModel.obtenerSucursalPorCodigo(codigo)
        sucursalCargada?.let {
            sucursal = it
            binding.tvTitulo.text = "Inventario: ${it.nombre}"
        }
    }

    private fun setupUI() {
        binding.btnAgregarProducto.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java).apply {
                putExtra("sucursal_codigo", sucursal.codigo)
            }
            startActivityForResult(intent, 1)
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.btnBuscarInventario.setOnClickListener {
            val termino = binding.etBuscarInventario.text.toString().trim()
            if (termino.isNotEmpty()) {
                buscarEnInventario(termino)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = InventarioAdapter(this)
        binding.rvInventario.layoutManager = LinearLayoutManager(this)
        binding.rvInventario.adapter = adapter

        // Cargar inventario inicial
        val inventario = sucursal.obtenerRepuestos()
        adapter.submitList(inventario)
    }

    private fun actualizarEstadisticas() {
        val inventario = sucursal.obtenerRepuestos()
        val totalProductos = inventario.size
        val totalStock = inventario.sumOf { it.stock }
        val valorAproximado = inventario.sumOf { it.stock * 100 } // Ejemplo: cada unidad vale 100

        binding.tvEstadisticas.text = """
            Productos distintos: $totalProductos
            Stock total: $totalStock unidades
            Valor aproximado: $${valorAproximado}
        """.trimIndent()
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
            val inventarioActual = sucursal.obtenerRepuestos()
            adapter.submitList(inventarioActual)
            actualizarEstadisticas()
        }
    }

    override fun onVerDetalleClick(repuesto: Repuesto) {
        // Podrías mostrar un diálogo con detalles del repuesto en esta sucursal
        // Por ahora, solo mostramos un mensaje
        android.app.AlertDialog.Builder(this)
            .setTitle("Detalle del Repuesto")
            .setMessage("Serie: ${repuesto.serie}\nDescripción: ${repuesto.descripcion}\nStock en esta sucursal: ${repuesto.stock}")
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onModificarStockClick(repuesto: Repuesto) {
        val intent = Intent(this, ModificarStockActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
            putExtra("repuesto_id", repuesto.id)
            putExtra("stock_actual", repuesto.stock)
        }
        startActivityForResult(intent, 2)
    }

    override fun onEliminarProductoClick(repuesto: Repuesto) {
        val intent = Intent(this, ConfirmarEliminacionSucursalActivity::class.java).apply {
            putExtra("sucursal_codigo", sucursal.codigo)
            putExtra("repuesto_id", repuesto.id)
            putExtra("tipo", "producto_sucursal")
        }
        startActivityForResult(intent, 3)
    }
}