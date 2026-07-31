package com.ister.conjuntoya.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VivitaLightColors = lightColorScheme(
    primary = VivitaVerdeBotella,
    onPrimary = VivitaBeigeClaro,
    secondary = VivitaCafeOscuro,
    onSecondary = VivitaBeigeClaro,
    tertiary = VivitaCafeOscuro,
    onTertiary = VivitaBeigeClaro,
    error = VivitaError,
    onError = Color.White,
    background = VivitaBeigeClaro,
    onBackground = VivitaTextoNegro,
    surface = VivitaBeigeSuave,
    onSurface = VivitaTextoNegro,
    surfaceVariant = VivitaBeigeSuave,
    onSurfaceVariant = VivitaTextoNegro
)

private val VivitaDarkColors = darkColorScheme(
    primary = Color(0xFF3E7A5E),
    onPrimary = VivitaBeigeClaro,
    secondary = VivitaCafeOscuro,
    onSecondary = VivitaBeigeClaro,
    tertiary = VivitaCafeOscuro,
    onTertiary = VivitaBeigeClaro,
    error = VivitaError,
    onError = Color.White,
    background = VivitaCafeMasOscuro,
    onBackground = VivitaBeigeClaro,
    surface = VivitaCafeOscuro,
    onSurface = VivitaBeigeClaro,
    surfaceVariant = VivitaCafeOscuro,
    onSurfaceVariant = VivitaBeigeClaro
)

@Composable
fun VivitaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) VivitaDarkColors else VivitaLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VivitaTypography,
        content = content
    )
}
