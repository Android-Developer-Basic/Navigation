package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class CookBookAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewTypes.RECIPE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_item, parent, false)
                RecipeViewHolder(view, itemClickListener)
            }

            ViewTypes.CATEGORY.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_category, parent, false)
                CategoryViewHolder(view)
            }

            else -> throw IllegalArgumentException("View type is not supported")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecipeViewHolder -> holder.bind(getItem(position) as RecipeListItem.RecipeItem)
            is CategoryViewHolder -> holder.bind(getItem(position) as RecipeListItem.CategoryItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecipeListItem.CategoryItem -> ViewTypes.CATEGORY.ordinal
            is RecipeListItem.RecipeItem -> ViewTypes.RECIPE.ordinal
        }
    }
}

enum class ViewTypes {
    CATEGORY,
    RECIPE
}

class DiffCallback : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> oldItem.id == newItem.id
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem ->
                oldItem.name == newItem.name

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }

}