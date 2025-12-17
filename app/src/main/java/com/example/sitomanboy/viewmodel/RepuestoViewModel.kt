package com.example.sitomanboy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sitomanboy.model.Repuesto

class RepuestoViewModel : ViewModel() {

    private val _repuestos = MutableLiveData<MutableList<Repuesto>>(mutableListOf())
    val repuestos: LiveData<MutableList<Repuesto>> = _repuestos

    private var contadorId = 1

    fun agregarRepuesto(repuesto: Repuesto) {
        val nuevaLista = _repuestos.value?.toMutableList() ?: mutableListOf()
        repuesto.id = contadorId++
        nuevaLista.add(repuesto)
        _repuestos.value = nuevaLista
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

    fun buscarRepuestos(serieONombre: String): List<Repuesto> {
        return _repuestos.value?.filter {
            it.serie.contains(serieONombre, ignoreCase = true) ||
                    it.descripcion.contains(serieONombre, ignoreCase = true)
        } ?: emptyList()
    }
}