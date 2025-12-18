package com.example.sitomanboy.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repuesto(
    var id: Int = 0,
    var serie: String = "",
    var descripcion: String = "",
    var stock: Int = 0
) : Parcelable {

    // Validación básica de datos
    fun esValido(): Boolean {
        return serie.isNotBlank() && descripcion.isNotBlank() && stock >= 0
    }

    override fun toString(): String {
        return "$serie - $descripcion (Stock: $stock)"
    }
}