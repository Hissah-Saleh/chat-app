package com.example.chatapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


private val LightColorScheme = lightColorScheme(
    primary               = Color(0xFF9575CD), // mid-tone lavender
    onPrimary             = Color(0xFFFFFFFF),
    primaryContainer      = Color(0xFFEDE7F6), // pale lilac
    onPrimaryContainer    = Color(0xFF311B92),

    secondary             = Color(0xFFCE93D8), // soft orchid
    onSecondary           = Color(0xFFFFFFFF),
    secondaryContainer    = Color(0xFFF3E5F5), // whisper-purple
    onSecondaryContainer  = Color(0xFF4A148C),

    background            = Color(0xFFF8F3FC), // almost-white lavender
    onBackground          = Color(0xFF332E3B),

    surface               = Color(0xFFFFFFFF),
    onSurface             = Color(0xFF332E3B),

    error                 = Color(0xFFB00020),
    onError               = Color(0xFFFFFFFF),

    outline               = Color(0xFFB39DDB)  // muted lavender gray
)

private val DarkColorScheme = darkColorScheme(
    primary               = Color(0xFFD1C4E9), // light lavender
    onPrimary             = Color(0xFF4527A0),
    primaryContainer      = Color(0xFF5E35B1),
    onPrimaryContainer    = Color(0xFFEDE7F6),

    secondary             = Color(0xFFE1BEE7),
    onSecondary           = Color(0xFF4A148C),
    secondaryContainer    = Color(0xFF7E57C2),
    onSecondaryContainer  = Color(0xFFF3E5F5),

    background            = Color(0xFF1E1B28),
    onBackground          = Color(0xFFEDE7F6),

    surface               = Color(0xFF2A2735),
    onSurface             = Color(0xFFEDE7F6),

    error                 = Color(0xFFCF6679),
    onError               = Color(0xFF000000),

    outline               = Color(0xFF8E7CC3)
)

    @Composable
fun ChatAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}