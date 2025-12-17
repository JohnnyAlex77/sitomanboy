package com.example.sitomanboy.sucursal

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.R
import com.example.sitomanboy.databinding.ActivityAgregarProductoBinding
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.viewmodel.RepuestoViewModel
import com.example.sitomanboy.viewmodel.SucursalViewModel

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private lateinit var sucursalViewModel: SucursalViewModel
    private lateinit var repuestoViewModel: RepuestoViewModel
    private lateinit var codigoSucursal: String
    private var repuestoSeleccionado: Repuesto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sucursalViewModel = ViewModelProvider(this)[SucursalViewModel::class.java]
        repuestoViewModel = ViewModelProvider(this)[RepuestoViewModel::class.java]

        codigoSucursal = intent.getStringExtra("sucursal_codigo") ?: ""

        setupUI()
        cargarRepuestosDisponibles()
    }

    private fun setupUI() {
        binding.btnAgregar.setOnClickListener {
            agregarProductoASucursal()
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        // Spinner para seleccionar repuesto
        binding.spinnerRepuestos.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position > 0) { // Posición 0 es el texto de instrucción
                    val repuestos = repuestoViewModel.repuestos.value ?: emptyList()
                    if (position - 1 < repuestos.size) {
                        repuestoSeleccionado = repuestos[position - 1]
                        binding.etStock.isEnabled = true
                    }
                } else {
                    repuestoSeleccionado = null
                    binding.etStock.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                repuestoSeleccionado = null
            }
        }
    }

    private fun cargarRepuestosDisponibles() {
        repuestoViewModel.repuestos.observe(this) { repuestos ->
            // Filtrar repuestos que ya están en la sucursal
            val sucursal = sucursalViewModel.obtenerSucursalPorCodigo(codigoSucursal)
            val repuestosEnSucursal = sucursal?.obtenerRepuestos()?.map { it.id } ?: emptyList()

            val repuestosDisponibles = repuestos.filter { !repuestosEnSucursal.contains(it.id) }

            val items = mutableListOf("Seleccione un repuesto...")
            items.addAll(repuestosDisponibles.map { "${it.serie} - ${it.descripcion}" })

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRepuestos.adapter = adapter
        }
    }

    private fun agregarProductoASucursal() {
        val stockText = binding.etStock.text.toString().trim()

        if (repuestoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un repuesto", Toast.LENGTH_SHORT).show()
            return
        }

        if (stockText.isEmpty()) {
            Toast.makeText(this, "Ingrese el stock inicial", Toast.LENGTH_SHORT).show()
            return
        }

        val stock = try {
            stockText.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Stock debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (stock < 0) {
            Toast.makeText(this, "Stock no puede ser negativo", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear una copia del repuesto con el stock específico para esta sucursal
        val repuestoParaSucursal = repuestoSeleccionado!!.copy(stock = stock)

        val exito = sucursalViewModel.agregarRepuestoASucursal(codigoSucursal, repuestoParaSucursal)

        if (exito) {
            Toast.makeText(this, "Producto agregado a la sucursal", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Error al agregar producto", Toast.LENGTH_SHORT).show()
        }
    }
}