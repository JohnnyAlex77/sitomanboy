package com.example.sitomanboy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sitomanboy.model.Repuesto
import com.example.sitomanboy.model.Sucursal

class SucursalViewModel : ViewModel() {

    private val _sucursales = MutableLiveData<MutableList<Sucursal>>(mutableListOf())
    val sucursales: LiveData<MutableList<Sucursal>> = _sucursales

    init {
        // Inicializar con datos de ejemplo
        val listaInicial = mutableListOf<Sucursal>()

        val sucursal1 = Sucursal("S001", "Sucursal Centro", "Calle Principal #123")
        sucursal1.agregarRepuesto(Repuesto(1, "R001", "Filtro de aceite", 20))
        sucursal1.agregarRepuesto(Repuesto(2, "R002", "Pastillas de freno", 15))
        listaInicial.add(sucursal1)

        val sucursal2 = Sucursal("S002", "Sucursal Norte", "Avenida Norte #456")
        sucursal2.agregarRepuesto(Repuesto(3, "R003", "Bujía", 30))
        listaInicial.add(sucursal2)

        val sucursal3 = Sucursal("S003", "Sucursal Sur", "Calle Sur #789")
        sucursal3.agregarRepuesto(Repuesto(4, "R004", "Filtro de aire", 10))
        sucursal3.agregarRepuesto(Repuesto(5, "R005", "Correa de distribución", 5))
        listaInicial.add(sucursal3)

        _sucursales.value = listaInicial
    }

    fun buscarSucursales(termino: String): List<Sucursal> {
        val terminoBusqueda = termino.trim()
        if (terminoBusqueda.isEmpty()) {
            return _sucursales.value ?: emptyList()
        }

        val listaActual = _sucursales.value ?: emptyList()
        return listaActual.filter { sucursal ->
            sucursal.codigo.contains(terminoBusqueda, ignoreCase = true) ||
                    sucursal.nombre.contains(terminoBusqueda, ignoreCase = true) ||
                    sucursal.direccion.contains(terminoBusqueda, ignoreCase = true)
        }
    }

    fun agregarSucursal(sucursal: Sucursal): Boolean {
        try {
            // Verificar si el código ya existe
            val codigoExistente = _sucursales.value?.any { it.codigo == sucursal.codigo } ?: false
            if (codigoExistente) {
                return false
            }

            val nuevaLista = _sucursales.value?.toMutableList() ?: mutableListOf()
            nuevaLista.add(sucursal)
            _sucursales.value = nuevaLista
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun actualizarSucursal(sucursalActualizada: Sucursal): Boolean {
        val lista = _sucursales.value ?: return false
        val index = lista.indexOfFirst { it.codigo == sucursalActualizada.codigo }

        if (index != -1) {
            // Mantener los repuestos de la sucursal original
            val sucursalOriginal = lista[index]
            val repuestosOriginales = sucursalOriginal.obtenerRepuestos()

            // Crear nueva sucursal con los repuestos
            val nuevaSucursal = Sucursal(
                codigo = sucursalActualizada.codigo,
                nombre = sucursalActualizada.nombre,
                direccion = sucursalActualizada.direccion
            )

            repuestosOriginales.forEach { repuesto ->
                nuevaSucursal.agregarRepuesto(repuesto)
            }

            lista[index] = nuevaSucursal
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

        // Actualizar la lista en el LiveData
        if (sucursal != null) {
            _sucursales.value = _sucursales.value
        }

        return sucursal != null
    }

    fun eliminarRepuestoDeSucursal(codigoSucursal: String, idRepuesto: Int): Boolean {
        val sucursal = obtenerSucursalPorCodigo(codigoSucursal)
        val resultado = sucursal?.eliminarRepuestoPorId(idRepuesto) ?: false

        if (resultado) {
            _sucursales.value = _sucursales.value
        }

        return resultado
    }

    fun actualizarStockEnSucursal(codigoSucursal: String, idRepuesto: Int, nuevoStock: Int): Boolean {
        val sucursal = obtenerSucursalPorCodigo(codigoSucursal)
        val resultado = sucursal?.actualizarStockRepuesto(idRepuesto, nuevoStock) ?: false

        if (resultado) {
            _sucursales.value = _sucursales.value
        }

        return resultado
    }

    fun buscarStockRepuesto(serieONombre: String): Map<Sucursal, Int> {
        val resultado = mutableMapOf<Sucursal, Int>()
        val terminoBusqueda = serieONombre.trim()

        if (terminoBusqueda.isEmpty()) {
            return resultado
        }

        _sucursales.value?.forEach { sucursal ->
            sucursal.obtenerRepuestos().forEach { repuesto ->
                if (repuesto.serie.contains(terminoBusqueda, ignoreCase = true) ||
                    repuesto.descripcion.contains(terminoBusqueda, ignoreCase = true)) {
                    resultado[sucursal] = repuesto.stock
                }
            }
        }

        return resultado
    }

    fun obtenerTodasSucursales(): List<Sucursal> {
        return _sucursales.value ?: emptyList()
    }
}