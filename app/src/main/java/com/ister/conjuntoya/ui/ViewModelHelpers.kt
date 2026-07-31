package com.ister.conjuntoya.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ister.conjuntoya.AppContainer
import com.ister.conjuntoya.ConjuntoYaApp

@Composable
fun appContainer(): AppContainer {
    val context = LocalContext.current.applicationContext as ConjuntoYaApp
    return context.container
}

fun Context.appContainer(): AppContainer = (applicationContext as ConjuntoYaApp).container
