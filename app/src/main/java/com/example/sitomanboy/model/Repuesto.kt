package com.example.sitomanboy.model

import java.io.Serializable

data class Repuesto(
    var id: Int = 0,
    var serie: String = "",
    var descripcion: String = "",
    var stock: Int = 0
) : Serializable {

    // Validación básica de datos
    fun esValido(): Boolean {
        return serie.isNotBlank() && descripcion.isNotBlank() && stock >= 0
    }

    override fun toString(): String {
        return "$serie - $descripcion (Stock: $stock)"
    }
}