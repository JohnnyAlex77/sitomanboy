package com.example.sitomanboy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sucursal(
    var codigo: String = "",
    var nombre: String = "",
    var direccion: String = "",
    private val _repuestos: MutableList<Repuesto> = mutableListOf()
) : Parcelable {

    // Lista privada para encapsulamiento
    private val repuestos: MutableList<Repuesto> = _repuestos

    // Métodos para manejar la lista de repuestos (encapsulamiento)
    fun agregarRepuesto(repuesto: Repuesto) {
        repuestos.add(repuesto)
    }

    fun eliminarRepuesto(repuesto: Repuesto): Boolean {
        return repuestos.remove(repuesto)
    }

    fun eliminarRepuestoPorId(id: Int): Boolean {
        val repuesto = repuestos.find { it.id == id }
        return repuesto?.let { repuestos.remove(it) } ?: false
    }

    fun obtenerRepuestos(): List<Repuesto> {
        return ArrayList(repuestos) // Retornar copia para mantener encapsulamiento
    }

    fun obtenerRepuestoPorId(id: Int): Repuesto? {
        return repuestos.find { it.id == id }
    }

    fun actualizarStockRepuesto(id: Int, nuevoStock: Int): Boolean {
        val repuesto = repuestos.find { it.id == id }
        repuesto?.let {
            it.stock = nuevoStock
            return true
        }
        return false
    }

    fun obtenerStockTotal(): Int {
        return repuestos.sumOf { it.stock }
    }

    fun obtenerCantidadRepuestos(): Int {
        return repuestos.size
    }

    // Validación básica de datos
    fun esValida(): Boolean {
        return codigo.isNotBlank() && nombre.isNotBlank() && direccion.isNotBlank()
    }

    override fun toString(): String {
        return "$codigo - $nombre"
    }
}