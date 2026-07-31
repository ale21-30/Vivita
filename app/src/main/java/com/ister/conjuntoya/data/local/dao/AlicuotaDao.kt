package com.ister.conjuntoya.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ister.conjuntoya.data.local.entity.AlicuotaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlicuotaDao {

    @Query("SELECT * FROM alicuotas ORDER BY anio DESC, mes DESC")
    fun obtenerTodas(): Flow<List<AlicuotaEntity>>

    @Query("SELECT * FROM alicuotas WHERE pagado = 0 ORDER BY anio, mes")
    fun obtenerPendientes(): Flow<List<AlicuotaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(alicuota: AlicuotaEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodas(alicuotas: List<AlicuotaEntity>)

    @Query("UPDATE alicuotas SET pagado = 1, fechaPago = :fecha WHERE id = :id")
    suspend fun marcarComoPagada(id: Long, fecha: String)

    @Query("SELECT COUNT(*) FROM alicuotas")
    suspend fun contar(): Int
}
