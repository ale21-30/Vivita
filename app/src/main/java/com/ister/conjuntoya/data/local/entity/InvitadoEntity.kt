package com.ister.conjuntoya.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invitados")
data class InvitadoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val cedula: String,
    val fechaVisita: String,
    val horaVisita: String,
    val fotoUri: String? = null,
    val codigoQr: String,
    val ingresado: Boolean = false,
    val fechaIngreso: String? = null
)
