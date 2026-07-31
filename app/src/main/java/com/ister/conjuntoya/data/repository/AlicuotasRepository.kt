package com.ister.conjuntoya.data.repository

import com.ister.conjuntoya.data.local.dao.AlicuotaDao
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import kotlinx.coroutines.flow.Flow

class AlicuotasRepository(private val dao: AlicuotaDao) {

    fun obtenerAlicuotas(): Flow<List<AlicuotaEntity>> = dao.obtenerTodas()

    fun obtenerPendientes(): Flow<List<AlicuotaEntity>> = dao.obtenerPendientes()

    suspend fun marcarComoPagada(id: Long, fecha: String) = dao.marcarComoPagada(id, fecha)

    suspend fun registrarAlicuota(alicuota: AlicuotaEntity) = dao.insertar(alicuota)
}
