package com.ister.conjuntoya.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ister.conjuntoya.ui.ajustes.AjustesScreen
import com.ister.conjuntoya.ui.ajustes.AjustesViewModel
import com.ister.conjuntoya.ui.alicuotas.AlicuotaDetalleScreen
import com.ister.conjuntoya.ui.alicuotas.AlicuotasScreen
import com.ister.conjuntoya.ui.basura.BasuraScreen
import com.ister.conjuntoya.ui.home.HomeScreen
import com.ister.conjuntoya.ui.invitados.InvitadoDetalleScreen
import com.ister.conjuntoya.ui.invitados.InvitadosScreen
import com.ister.conjuntoya.ui.invitados.RegistrarInvitadoScreen
import com.ister.conjuntoya.ui.porteria.PorteriaScreen

private val destinosBarraInferior = listOf(
    DestinoBarraInferior(Screen.Home, "Inicio", Icons.Filled.Home),
    DestinoBarraInferior(Screen.Alicuotas, "Alícuotas", Icons.Filled.Payments),
    DestinoBarraInferior(Screen.Invitados, "Invitados", Icons.Filled.Groups),
    DestinoBarraInferior(Screen.Porteria, "Portería", Icons.Filled.QrCodeScanner),
    DestinoBarraInferior(Screen.Ajustes, "Ajustes", Icons.Filled.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(ajustesViewModel: AjustesViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route
    val mostrarBarraInferior = destinosBarraInferior.any { it.screen.route == rutaActual }

    Scaffold(
        bottomBar = {
            if (mostrarBarraInferior) {
                NavigationBar {
                    destinosBarraInferior.forEach { destino ->
                        NavigationBarItem(
                            selected = rutaActual == destino.screen.route,
                            onClick = { navegarATopLevel(navController, destino.screen.route) },
                            icon = { Icon(destino.icono, contentDescription = destino.etiqueta) },
                            label = { Text(destino.etiqueta) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(onVerBasura = { navController.navigate(Screen.Basura.route) })
            }
            composable(Screen.Basura.route) {
                BasuraScreen(onVolver = { navController.popBackStack() })
            }
            composable(Screen.Alicuotas.route) {
                AlicuotasScreen(onAbrirDetalle = { id ->
                    navController.navigate(Screen.AlicuotaDetalle.crearRuta(id))
                })
            }
            composable(
                route = Screen.AlicuotaDetalle.route,
                arguments = listOf(navArgument("alicuotaId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("alicuotaId") ?: 0L
                AlicuotaDetalleScreen(alicuotaId = id, onVolver = { navController.popBackStack() })
            }
            composable(Screen.Invitados.route) {
                InvitadosScreen(
                    onRegistrar = { navController.navigate(Screen.RegistrarInvitado.route) },
                    onAbrirDetalle = { id -> navController.navigate(Screen.InvitadoDetalle.crearRuta(id)) }
                )
            }
            composable(Screen.RegistrarInvitado.route) {
                RegistrarInvitadoScreen(
                    onRegistrado = { id ->
                        navController.popBackStack()
                        navController.navigate(Screen.InvitadoDetalle.crearRuta(id))
                    },
                    onVolver = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.InvitadoDetalle.route,
                arguments = listOf(navArgument("invitadoId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("invitadoId") ?: 0L
                InvitadoDetalleScreen(invitadoId = id, onVolver = { navController.popBackStack() })
            }
            composable(Screen.Porteria.route) {
                PorteriaScreen()
            }
            composable(Screen.Ajustes.route) {
                AjustesScreen(viewModel = ajustesViewModel)
            }
        }
    }
}

private fun navegarATopLevel(navController: NavHostController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
