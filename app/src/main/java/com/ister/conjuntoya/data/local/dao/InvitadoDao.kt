package com.ister.conjuntoya.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvitadoDao {

    @Query("SELECT * FROM invitados ORDER BY fechaVisita DESC, horaVisita DESC")
    fun obtenerTodos(): Flow<List<InvitadoEntity>>

    @Query("SELECT * FROM invitados WHERE ingresado = 0 ORDER BY fechaVisita, horaVisita")
    fun obtenerPendientesDeIngreso(): Flow<List<InvitadoEntity>>

    @Query("SELECT * FROM invitados WHERE codigoQr = :codigo LIMIT 1")
    suspend fun obtenerPorCodigoQr(codigo: String): InvitadoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(invitado: InvitadoEntity): Long

    @Query("UPDATE invitados SET ingresado = 1, fechaIngreso = :fecha WHERE id = :id")
    suspend fun marcarIngreso(id: Long, fecha: String)

    @Query("DELETE FROM invitados WHERE id = :id")
    suspend fun eliminar(id: Long)
}
