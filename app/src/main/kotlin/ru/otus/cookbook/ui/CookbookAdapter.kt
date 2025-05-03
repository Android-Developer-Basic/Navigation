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


class CookbookAdapter(
    private val onRecipeClick: (Int) -> Unit
) :
    ListAdapter<RecipeListItem, CookbookAdapter.CookbookViewHolder>(RecipeDiff) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = when (viewType) {
        RecipeListItem.CategoryItem.layoutId -> CookbookViewHolder.CategoryHolder(
            VhRecipeCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        RecipeListItem.RecipeItem.layoutId -> CookbookViewHolder.RecipeHolder(
            VhRecipeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onRecipeClick
        )

        else -> throw IllegalArgumentException("View is not supported")
    }

    override fun onBindViewHolder(holder: CookbookAdapter.CookbookViewHolder, position: Int) {
        when (holder) {
            is CookbookViewHolder.CategoryHolder -> holder.bind(getItem(position) as RecipeListItem.CategoryItem)
            is CookbookViewHolder.RecipeHolder -> holder.bind(getItem(position) as RecipeListItem.RecipeItem)
        }
    }

    sealed class CookbookViewHolder(view: View) : ViewHolder(view) {
        class RecipeHolder(
            private val binding: VhRecipeItemBinding,
            private val onClick: (Int) -> Unit
        ) : CookbookViewHolder(binding.root) {
            private var id: Int = -1

            init {
                binding.root.setOnClickListener { onClick(id) }
            }

            fun bind(recipe: RecipeListItem.RecipeItem) = with(binding) {
                this@RecipeHolder.id = recipe.id
                txtRecipeAvatar.text = recipe.title[0].uppercase()
                txtRecipeName.text = recipe.title
                txtRecipeDescription.text = recipe.description
                Glide.with(root.context).load(recipe.imageUrl).centerCrop().into(imgRecipeImage)
            }

        }

        class CategoryHolder(
            private val binding: VhRecipeCategoryBinding
        ) :
            CookbookViewHolder(binding.root) {

            fun bind(category: RecipeListItem.CategoryItem) = with(binding) {
                txtCategoryName.text = category.name
            }
        }
    }

}

private object RecipeDiff : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> oldItem.id == newItem.id
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem -> oldItem.category == newItem.category
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}


