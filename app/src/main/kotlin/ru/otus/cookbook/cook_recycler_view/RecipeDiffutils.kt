package ru.otus.cookbook.cook_recycler_view

import androidx.recyclerview.widget.DiffUtil
import ru.otus.cookbook.data.RecipeListItem

class RecipeDiffutils : DiffUtil.ItemCallback<RecipeListItem>() {

    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem== newItem
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}