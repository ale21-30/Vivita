package com.ister.conjuntoya.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Alicuotas : Screen("alicuotas")
    object AlicuotaDetalle : Screen("alicuotas/{alicuotaId}") {
        fun crearRuta(id: Long) = "alicuotas/$id"
    }
    object Basura : Screen("basura")
    object Invitados : Screen("invitados")
    object RegistrarInvitado : Screen("invitados/registrar")
    object InvitadoDetalle : Screen("invitados/{invitadoId}") {
        fun crearRuta(id: Long) = "invitados/$id"
    }
    object Porteria : Screen("porteria")
    object Ajustes : Screen("ajustes")
}

data class DestinoBarraInferior(
    val screen: Screen,
    val etiqueta: String,
    val icono: androidx.compose.ui.graphics.vector.ImageVector
)
