package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkPurple,
    onPrimary = Color.Black,
    primaryContainer = DarkPurpleContainer,
    onPrimaryContainer = Color.White,
    secondary = DarkGreen,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = BrandAmber,
    background = DarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = DarkSurface,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BrandPurple,
    onPrimary = Color.White,
    primaryContainer = BrandPurpleLight,
    onPrimaryContainer = BrandPurpleDark,
    secondary = BrandGreen,
    onSecondary = Color.White,
    secondaryContainer = BrandGreenLight,
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = BrandAmber,
    onTertiary = Color.White,
    tertiaryContainer = BrandAmberLight,
    onTertiaryContainer = Color(0xFF92400E),
    background = BackgroundLight,
    onBackground = NeutralDark,
    surface = SurfaceCard,
    onSurface = NeutralDark,
    surfaceVariant = NeutralLight,
    onSurfaceVariant = NeutralMedium,
    outline = NeutralBorder
  )

@Composable
fun DeepakStoreTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

