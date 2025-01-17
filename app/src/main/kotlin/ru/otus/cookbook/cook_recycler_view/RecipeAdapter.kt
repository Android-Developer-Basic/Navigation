package ru.otus.cookbook.cook_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class RecipeAdapter : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(RecipeDiffutils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            ViewType.CATEGORY.id -> {
                CategoryViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vh_recipe_category, parent, false)
                )
            }

            ViewType.RECIPE.id -> {
                RecipeViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.vh_recipe_item, parent, false)
                )
            }

            else -> throw IllegalArgumentException("wrong type of view")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecipeListItem.CategoryItem -> ViewType.CATEGORY.id
            is RecipeListItem.RecipeItem -> ViewType.RECIPE.id
            else -> -1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (val item = currentList.getOrNull(position)) {
            is RecipeListItem.CategoryItem -> {
                (holder as CategoryViewHolder).bind(item)
            }

            is RecipeListItem.RecipeItem -> {
                (holder as RecipeViewHolder).bind(item)
            }

            else ->
                throw IllegalArgumentException("wrong type of holder")
        }
    }

    fun changeRecipeItems(newChatList: List<RecipeListItem>) {
        this.submitList(newChatList)
        currentList
        println()
    }

    enum class ViewType(val id: Int) {
        CATEGORY(0),
        RECIPE(1)
    }
}