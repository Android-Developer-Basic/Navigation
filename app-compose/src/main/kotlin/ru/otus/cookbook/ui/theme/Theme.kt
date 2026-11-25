package ru.otus.cookbook.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = CookbookPrimary,
    onPrimary = CookbookOnPrimary,
    background = CookbookBackground,
    onBackground = CookbookOnSurface,
    surface = CookbookSurface,
    onSurface = CookbookOnSurface,
    onSurfaceVariant = CookbookOnSurfaceVariant,
    outline = CookbookOutline,
    secondaryContainer = CookbookImagePlaceholder,
    onSecondaryContainer = CookbookPrimary,
)

@Composable
fun CookbookTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
