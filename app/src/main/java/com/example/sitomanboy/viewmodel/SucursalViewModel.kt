package com.example.sitomanboy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.model.Sucursal

class SucursalViewModel : ViewModel() {

    private val _sucursales = MutableLiveData<MutableList<Sucursal>>(mutableListOf())
    val sucursales: LiveData<MutableList<Sucursal>> = _sucursales

    // ✅ NUEVO MÉTODO AGREGADO - Para buscar sucursales
    fun buscarSucursales(termino: String): List<Sucursal> {
        val listaActual = _sucursales.value ?: emptyList()
        return listaActual.filter { sucursal ->
            sucursal.codigo.contains(termino, ignoreCase = true) ||
                    sucursal.nombre.contains(termino, ignoreCase = true) ||
                    sucursal.direccion.contains(termino, ignoreCase = true)
        }
    }

    fun agregarSucursal(sucursal: Sucursal) {
        val nuevaLista = _sucursales.value?.toMutableList() ?: mutableListOf()
        nuevaLista.add(sucursal)
        _sucursales.value = nuevaLista
    }

    fun actualizarSucursal(sucursalActualizada: Sucursal): Boolean {
        val lista = _sucursales.value ?: return false
        val index = lista.indexOfFirst { it.codigo == sucursalActualizada.codigo }

        if (index != -1) {
            // Mantener los repuestos de la sucursal original
            val repuestosOriginales = lista[index].obtenerRepuestos()
            repuestosOriginales.forEach { repuesto ->
                sucursalActualizada.agregarRepuesto(repuesto)
            }

            lista[index] = sucursalActualizada
            _sucursales.value = lista
            return true
        }
        return false
    }

    fun eliminarSucursal(codigo: String): Boolean {
        val lista = _sucursales.value ?: return false
        val sucursal = lista.find { it.codigo == codigo }

        sucursal?.let {
            lista.remove(it)
            _sucursales.value = lista
            return true
        }
        return false
    }

    fun obtenerSucursalPorCodigo(codigo: String): Sucursal? {
        return _sucursales.value?.find { it.codigo == codigo }
    }

    fun agregarRepuestoASucursal(codigoSucursal: String, repuesto: Repuesto): Boolean {
        val sucursal = obtenerSucursalPorCodigo(codigoSucursal)
        sucursal?.agregarRepuesto(repuesto)
        return sucursal != null
    }

    fun eliminarRepuestoDeSucursal(codigoSucursal: String, idRepuesto: Int): Boolean {
        val sucursal = obtenerSucursalPorCodigo(codigoSucursal)
        return sucursal?.eliminarRepuestoPorId(idRepuesto) ?: false
    }

    fun actualizarStockEnSucursal(codigoSucursal: String, idRepuesto: Int, nuevoStock: Int): Boolean {
        val sucursal = obtenerSucursalPorCodigo(codigoSucursal)
        return sucursal?.actualizarStockRepuesto(idRepuesto, nuevoStock) ?: false
    }

    fun buscarStockRepuesto(serieONombre: String): Map<Sucursal, Int> {
        val resultado = mutableMapOf<Sucursal, Int>()

        _sucursales.value?.forEach { sucursal ->
            sucursal.obtenerRepuestos().forEach { repuesto ->
                if (repuesto.serie.contains(serieONombre, ignoreCase = true) ||
                    repuesto.descripcion.contains(serieONombre, ignoreCase = true)) {
                    resultado[sucursal] = repuesto.stock
                }
            }
        }

        return resultado
    }
}