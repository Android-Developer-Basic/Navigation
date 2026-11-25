package ru.otus.cookbook.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CookbookRoute : NavKey

@Serializable
data class RecipeRoute(val recipeId: Int) : NavKey

@Serializable
data class DeleteRecipeRoute(
    val title: String,
    val recipeId: Int
) : NavKey
