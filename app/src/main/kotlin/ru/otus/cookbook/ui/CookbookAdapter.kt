package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


class CookbookAdapter(
    private val listener: Listener
) : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(DiffUtilItem()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecipeListItem.RecipeItem.layoutId -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_item, parent, false)
                RecipeViewHolder(view, listener)
            }

            RecipeListItem.CategoryItem.layoutId -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_category, parent, false)
                CategoryViewHolder(view)
            }

            else -> throw IllegalArgumentException("Not found view type for chat adapter")
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
            is RecipeListItem.RecipeItem -> RecipeListItem.RecipeItem.layoutId
            is RecipeListItem.CategoryItem -> RecipeListItem.CategoryItem.layoutId
            else -> -1
        }
    }
}

class DiffUtilItem : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        if (oldItem::class != newItem::class) return false

        return when {
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem -> oldItem.name == newItem.name
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> oldItem.id == newItem.id
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem -> oldItem.name == newItem.name
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.imageUrl == newItem.imageUrl
            else -> false
        }
    }

}