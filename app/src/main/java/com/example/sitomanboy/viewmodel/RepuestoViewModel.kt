package com.example.sitomanboy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sitomanboy.model.Repuesto

class RepuestoViewModel : ViewModel() {

    private val _repuestos = MutableLiveData<MutableList<Repuesto>>(mutableListOf())
    val repuestos: LiveData<MutableList<Repuesto>> = _repuestos

    // Datos de ejemplo para pruebas
    init {
        // Inicializar con algunos datos de ejemplo
        val listaInicial = mutableListOf<Repuesto>()
        listaInicial.add(Repuesto(1, "R001", "Filtro de aceite", 50))
        listaInicial.add(Repuesto(2, "R002", "Pastillas de freno", 30))
        listaInicial.add(Repuesto(3, "R003", "Bujía", 100))
        listaInicial.add(Repuesto(4, "R004", "Filtro de aire", 25))
        listaInicial.add(Repuesto(5, "R005", "Correa de distribución", 15))
        _repuestos.value = listaInicial
    }

    private var contadorId = 6 // Comenzar después de los datos de ejemplo

    fun agregarRepuesto(repuesto: Repuesto): Boolean {
        try {
            val nuevaLista = _repuestos.value?.toMutableList() ?: mutableListOf()
            repuesto.id = contadorId++
            nuevaLista.add(repuesto)
            _repuestos.value = nuevaLista
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun actualizarRepuesto(repuestoActualizado: Repuesto): Boolean {
        val lista = _repuestos.value ?: return false
        val index = lista.indexOfFirst { it.id == repuestoActualizado.id }

        if (index != -1) {
            lista[index] = repuestoActualizado
            _repuestos.value = lista
            return true
        }
        return false
    }

    fun eliminarRepuesto(id: Int): Boolean {
        val lista = _repuestos.value ?: return false
        val repuesto = lista.find { it.id == id }

        repuesto?.let {
            lista.remove(it)
            _repuestos.value = lista
            return true
        }
        return false
    }

    fun obtenerRepuestoPorId(id: Int): Repuesto? {
        return _repuestos.value?.find { it.id == id }
    }

    fun buscarRepuestos(termino: String): List<Repuesto> {
        val terminoBusqueda = termino.trim()
        if (terminoBusqueda.isEmpty()) {
            return _repuestos.value ?: emptyList()
        }

        return _repuestos.value?.filter {
            it.serie.contains(terminoBusqueda, ignoreCase = true) ||
                    it.descripcion.contains(terminoBusqueda, ignoreCase = true)
        } ?: emptyList()
    }

    fun obtenerTodosRepuestos(): List<Repuesto> {
        return _repuestos.value ?: emptyList()
    }
}