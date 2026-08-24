package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = AceGold,
  onPrimary = AceObsidian,
  primaryContainer = AceIndigoDark,
  onPrimaryContainer = AceTextPrimary,
  secondary = AceIndigoLight,
  onSecondary = AceObsidian,
  secondaryContainer = AceDarkCard,
  onSecondaryContainer = AceTextPrimary,
  tertiary = AceBlue,
  onTertiary = AceObsidian,
  background = AceObsidian,
  onBackground = AceTextPrimary,
  surface = AceDarkSurface,
  onSurface = AceTextPrimary,
  surfaceVariant = AceDarkCard,
  onSurfaceVariant = AceTextSecondary,
  outline = AceDarkCardBorder,
  error = AceRose,
  onError = AceTextPrimary
)

private val LightColorScheme = lightColorScheme(
  primary = AceGoldDark,
  onPrimary = AceLightSurface,
  primaryContainer = AceGoldLight,
  onPrimaryContainer = AceObsidian,
  secondary = AceIndigo,
  onSecondary = AceLightSurface,
  secondaryContainer = AceLightCard,
  onSecondaryContainer = AceLightTextPrimary,
  tertiary = AceBlue,
  onTertiary = AceLightSurface,
  background = AceLightBg,
  onBackground = AceLightTextPrimary,
  surface = AceLightSurface,
  onSurface = AceLightTextPrimary,
  surfaceVariant = AceLightCard,
  onSurfaceVariant = AceLightTextSecondary,
  outline = AceLightBorder,
  error = AceRose,
  onError = AceLightSurface
)

@Composable
fun AceTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> DarkColorScheme // ACE uses an exquisite dark obsidian aesthetic by default
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  AceTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
