package ru.otus.cookbook.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Provides access to the list of recipes.
 */
class RecipeRepository(recipes: List<Recipe>) {

    private val nextId = recipes.maxOfOrNull { it.id }?.plus(1) ?: 1
    private val recipes = MutableStateFlow(recipes)

    /**
     * Returns the list of recipes as a flow.
     */
    fun getRecipes(): StateFlow<List<Recipe>> = recipes.asStateFlow()

    /**
     * Returns the recipe with the specified ID.
     */
    fun getRecipe(id: Int): Recipe? = recipes.value.find { it.id == id }

    /**
     * Deletes the recipe with the specified ID.
     */
    fun deleteRecipe(id: Int) {
        recipes.update { list ->
            list.filter { it.id != id }
        }
    }

    /**
     * Adds a recipe to the list.
     */
    fun addRecipe(recipe: Recipe) {
        recipes.update { list ->
            list + recipe.copy(id = nextId)
        }
    }
}
