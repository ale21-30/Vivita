package com.ister.conjuntoya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ister.conjuntoya.navigation.MainScreen
import com.ister.conjuntoya.ui.ajustes.AjustesViewModel
import com.ister.conjuntoya.ui.theme.VivitaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as VivitaApp).container

        setContent {
            val ajustesViewModel: AjustesViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { AjustesViewModel(container.userPreferencesRepository) }
                }
            )
            val modoOscuro by ajustesViewModel.modoOscuro.collectAsStateWithLifecycle()

            VivitaTheme(darkTheme = modoOscuro) {
                MainScreen(ajustesViewModel = ajustesViewModel)
            }
        }
    }
}
