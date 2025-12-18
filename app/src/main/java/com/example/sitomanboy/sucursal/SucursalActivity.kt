package com.example.sitomanboy.sucursal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sitomanboy.databinding.ActivitySucursalBinding
import com.example.sitomanboy.viewmodel.SucursalViewModel

class SucursalActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySucursalBinding
    private lateinit var viewModel: SucursalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySucursalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SucursalViewModel::class.java]

        setupUI()
    }

    private fun setupUI() {
        binding.btnCrearSucursal.setOnClickListener {
            val intent = Intent(this, CrearSucursalActivity::class.java)
            startActivity(intent)
        }

        binding.btnListaSucursales.setOnClickListener {
            val intent = Intent(this, ListaSucursalesActivity::class.java)
            startActivity(intent)
        }

        // Configurar SearchView para búsqueda en tiempo real
        binding.searchViewSucursales.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val intent = Intent(this@SucursalActivity, ListaSucursalesActivity::class.java).apply {
                        putExtra("busqueda", query)
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Podríamos implementar búsqueda en tiempo real aquí si quisiéramos
                return false
            }
        })
    }
}