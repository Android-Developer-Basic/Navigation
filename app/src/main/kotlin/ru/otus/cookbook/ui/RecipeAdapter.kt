package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding
import java.util.Locale

/**
 * Recipe adapter
 */
class RecipeAdapter(private val onClick: (Int) -> Unit) : ListAdapter<RecipeListItem, RecipeAdapter.RecipeViewHolder>(RecipeDiff) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        RecipeListItem.RecipeItem.layoutId -> RecipeViewHolder.RecipeHolder(
            VhRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
        RecipeListItem.CategoryItem.layoutId -> RecipeViewHolder.CategoryHolder(
            VhRecipeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        else -> throw IllegalArgumentException("Unknown view type: $viewType")
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        when(holder) {
            is RecipeViewHolder.CategoryHolder -> holder.bind(getItem(position) as RecipeListItem.CategoryItem)
            is RecipeViewHolder.RecipeHolder -> holder.bind(getItem(position) as RecipeListItem.RecipeItem)
        }
    }

    sealed class RecipeViewHolder(view: View): ViewHolder(view) {

        class RecipeHolder(private val binding: VhRecipeItemBinding, private val onClick: (Int) -> Unit) : RecipeViewHolder(binding.root) {
            private var id: Int = -1

            init {
                binding.root.setOnClickListener { onClick(id) }
            }

            fun bind(recipe: RecipeListItem.RecipeItem) = with(binding) {
                this@RecipeHolder.id = recipe.id
                id.text = String.format(Locale.getDefault(), "%02d", recipe.id)
                title.text = recipe.title
                description.text = recipe.description
                Glide.with(itemView.context)
                    .load(recipe.imageUrl)
                    .into(imageViewRecipe)
            }
        }

        class CategoryHolder(private val binding: VhRecipeCategoryBinding) : RecipeViewHolder(binding.root) {
            fun bind(category: RecipeListItem.CategoryItem) = with(binding) {
                name.text = category.name
            }
        }
    }
}

private object RecipeDiff : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean = oldItem.isSame(newItem)
    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean = oldItem.isContentSame(newItem)
}