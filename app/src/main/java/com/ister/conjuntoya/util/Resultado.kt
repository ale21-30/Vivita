package com.ister.conjuntoya.util

sealed class Resultado<out T> {
    object Cargando : Resultado<Nothing>()
    data class Exito<T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
