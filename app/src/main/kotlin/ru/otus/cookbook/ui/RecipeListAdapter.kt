package ru.otus.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.VhRecipeCategoryBinding
import ru.otus.cookbook.databinding.VhRecipeItemBinding

class RecipeListAdapter(
    private val onRecipeClick: (Int) -> Unit
) : ListAdapter<RecipeListItem, RecyclerView.ViewHolder>(RecipeListItemDiff) {

    companion object {
        // Типы элементов списка
        private const val TYPE_CATEGORY = 0
        private const val TYPE_RECIPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecipeListItem.CategoryItem -> TYPE_CATEGORY
            is RecipeListItem.RecipeItem -> TYPE_RECIPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_CATEGORY -> {
                CategoryViewHolder(
                    VhRecipeCategoryBinding.inflate(inflater, parent, false)
                )
            }
            TYPE_RECIPE -> {
                RecipeViewHolder(
                    VhRecipeItemBinding.inflate(inflater, parent, false),
                    onRecipeClick
                )
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is RecipeListItem.CategoryItem -> {
                (holder as CategoryViewHolder).bind(item)
            }
            is RecipeListItem.RecipeItem -> {
                (holder as RecipeViewHolder).bind(item)
            }
        }
    }

    /**
     * ViewHolder для категории рецептов
     */
    class CategoryViewHolder(
        private val binding: VhRecipeCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecipeListItem.CategoryItem) {
            binding.name.text = item.name
        }
    }

    /**
     * ViewHolder для элемента рецепта
     */
    class RecipeViewHolder(
        private val binding: VhRecipeItemBinding,
        private val onRecipeClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var recipeId: Int = -1

        init {
            // Обработка клика на элемент рецепта
            binding.root.setOnClickListener {
                if (recipeId != -1) {
                    onRecipeClick(recipeId)
                }
            }
        }

        fun bind(item: RecipeListItem.RecipeItem) {
            recipeId = item.id
            // Отображение первой буквы названия рецепта
            binding.letterText.text = item.title.firstOrNull()?.uppercase().orEmpty()
            // Отображение заголовка рецепта
            binding.title.text = item.title

            // Загрузка изображения через Glide
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.previewImage)
        }
    }
}

private object RecipeListItemDiff : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return when {
            oldItem is RecipeListItem.CategoryItem && newItem is RecipeListItem.CategoryItem -> {
                oldItem.category == newItem.category
            }
            oldItem is RecipeListItem.RecipeItem && newItem is RecipeListItem.RecipeItem -> {
                oldItem.id == newItem.id
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean {
        return oldItem == newItem
    }
}


