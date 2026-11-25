package ru.otus.cookbook

import ru.otus.cookbook.data.RecipeRepository

/**
 * Provides [RecipeRepository] for ViewModel factories.
 * Implemented by Application classes in UI modules (Views / Compose).
 */
interface HasRecipeRepository {
    val recipeRepository: RecipeRepository
}
