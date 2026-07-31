package com.ister.conjuntoya.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alicuotas")
data class AlicuotaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mes: Int,
    val anio: Int,
    val monto: Double,
    val pagado: Boolean = false,
    val fechaPago: String? = null
)
