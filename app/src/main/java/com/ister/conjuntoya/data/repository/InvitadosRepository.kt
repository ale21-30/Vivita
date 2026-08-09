package com.ister.conjuntoya.data.repository

import com.ister.conjuntoya.data.local.dao.InvitadoDao
import com.ister.conjuntoya.data.local.entity.InvitadoEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class InvitadosRepository(private val dao: InvitadoDao) {

    fun obtenerInvitados(): Flow<List<InvitadoEntity>> = dao.obtenerTodos()

    fun obtenerPendientesDeIngreso(): Flow<List<InvitadoEntity>> = dao.obtenerPendientesDeIngreso()

    suspend fun registrarInvitado(
        nombre: String,
        cedula: String,
        fechaVisita: String,
        horaVisita: String,
        fotoUri: String?
    ): Long {
        val invitado = InvitadoEntity(
            nombre = nombre,
            cedula = cedula,
            fechaVisita = fechaVisita,
            horaVisita = horaVisita,
            fotoUri = fotoUri,
            codigoQr = UUID.randomUUID().toString()
        )
        return dao.insertar(invitado)
    }

    suspend fun validarIngresoPorQr(codigo: String, fecha: String): InvitadoEntity? {
        val invitado = dao.obtenerPorCodigoQr(codigo) ?: return null
        if (!invitado.ingresado) {
            dao.marcarIngreso(invitado.id, fecha)
        }
        return invitado
    }

    suspend fun eliminarInvitado(id: Long) {
        dao.eliminar(id)
    }
}
