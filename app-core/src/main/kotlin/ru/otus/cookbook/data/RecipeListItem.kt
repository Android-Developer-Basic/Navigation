package ru.otus.cookbook.data

/**
 * Recipe list items shared by Views and Compose UIs.
 */
sealed class RecipeListItem {
    /**
     * Recipe item.
     */
    data class RecipeItem(private val recipe: Recipe) : RecipeListItem() {
        val id: Int get() = recipe.id
        val title: String get() = recipe.title
        val description: String get() = recipe.description
        val imageUrl: String get() = recipe.imageUrl
    }

    /**
     * Category item.
     */
    data class CategoryItem(val category: RecipeCategory) : RecipeListItem() {
        val name: String get() = category.name
    }
}
