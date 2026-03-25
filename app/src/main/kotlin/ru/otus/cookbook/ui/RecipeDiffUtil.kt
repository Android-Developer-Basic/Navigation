package ru.otus.cookbook.ui

import androidx.recyclerview.widget.DiffUtil
import ru.otus.cookbook.data.RecipeListItem

class RecipeDiffUtil: DiffUtil.ItemCallback<RecipeListItem>() {

    override fun areItemsTheSame( firstItem: RecipeListItem, secondItem: RecipeListItem ): Boolean {

        return when{
            firstItem is RecipeListItem.CategoryItem && secondItem is RecipeListItem.CategoryItem ->
                firstItem.name == secondItem.name
            firstItem is RecipeListItem.RecipeItem && secondItem is RecipeListItem.RecipeItem ->
                firstItem.id == secondItem.id
            else -> false
        }

    }

    override fun areContentsTheSame(firsttem: RecipeListItem, secondItem: RecipeListItem): Boolean {

        return firsttem == secondItem

    }

}
