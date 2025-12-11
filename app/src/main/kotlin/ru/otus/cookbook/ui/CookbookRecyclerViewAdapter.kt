package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class CookbookRecyclerViewAdapter(
    val recipeListener: RecipeItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = emptyList<RecipeListItem>()

    fun setList(recipeList: List<RecipeListItem>) {
        list = recipeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.CATEGORY.id -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_category, parent, false)
                CategoryItemViewHolder(view)
            }

            ViewType.RECIPE.id -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vh_recipe_item, parent, false)
                RecipeItemViewHolder(
                    view = view,
                    recipeListener = recipeListener
                )
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is RecipeListItem.CategoryItem -> ViewType.CATEGORY.id
            is RecipeListItem.RecipeItem -> ViewType.RECIPE.id
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = list[position]

        when (getItemViewType(position)) {
            ViewType.CATEGORY.id -> (holder as CategoryItemViewHolder).bind(item as RecipeListItem.CategoryItem)
            ViewType.RECIPE.id -> (holder as RecipeItemViewHolder).bind(item as RecipeListItem.RecipeItem)
        }

    }

    override fun getItemCount(): Int = list.size

    private enum class ViewType(val id: Int) {
        CATEGORY(R.layout.vh_recipe_category),
        RECIPE(R.layout.vh_recipe_item)
    }
}