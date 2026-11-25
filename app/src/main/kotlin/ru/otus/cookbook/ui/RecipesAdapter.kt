package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding

class RecipesAdapter(
    private val onRecipeClick: (Int) -> Unit
) : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is RecipeListItem.CategoryItem -> R.layout.vh_recipe_category
        is RecipeListItem.RecipeItem -> R.layout.vh_recipe_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.vh_recipe_category -> CategoryViewHolder(
                VhRecipeCategoryBinding.inflate(inflater, parent, false)
            )
            R.layout.vh_recipe_item -> RecipeViewHolder(
                VhRecipeItemBinding.inflate(inflater, parent, false),
                onRecipeClick
            )
            else -> error("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecipeListItem.CategoryItem -> (holder as CategoryViewHolder).bind(item)
            is RecipeListItem.RecipeItem -> (holder as RecipeViewHolder).bind(item)
        }
    }

    class CategoryViewHolder(
        private val binding: VhRecipeCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecipeListItem.CategoryItem) {
            binding.categoryTitle.text = item.name
        }
    }

    class RecipeViewHolder(
        private val binding: VhRecipeItemBinding,
        private val onRecipeClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var recipeId: Int = -1

        init {
            binding.root.setOnClickListener { onRecipeClick(recipeId) }
        }

        fun bind(item: RecipeListItem.RecipeItem) = with(binding) {
            recipeId = item.id
            avatar.text = item.title.firstOrNull()?.uppercaseChar()?.toString().orEmpty()
            title.text = item.title
            description.text = item.description
            image.load(item.imageUrl) {
                crossfade(true)
                scale(Scale.FILL)
                placeholder(R.drawable.ic_recipe_placeholder)
                error(R.drawable.ic_recipe_placeholder)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RecipeListItem>() {
        override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean =
            when {
                oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem ->
                    oldItem.category == newItem.category
                oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem ->
                    oldItem.id == newItem.id
                else -> false
            }

        override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean =
            oldItem == newItem
    }
}
