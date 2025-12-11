package ru.otus.cookbook.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import coil3.load
import coil3.request.placeholder
import coil3.request.error

interface RecipeItemListener {
    fun onRecipeClick(recipeId: Int)
}

class CategoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView by lazy { view.findViewById<TextView>(R.id.category_item_name) }

    fun bind(categoryItem: RecipeListItem.CategoryItem) {
        name.text = categoryItem.name
    }
}

class RecipeItemViewHolder(
    view: View,
    val recipeListener: RecipeItemListener
) : RecyclerView.ViewHolder(view) {
    private val letter: TextView by lazy { view.findViewById(R.id.recipe_letter) }
    private val title: TextView by lazy { view.findViewById(R.id.recipe_item_title) }
    private val description: TextView by lazy { view.findViewById(R.id.recipe_item_description) }
    private val image: ImageView by lazy { view.findViewById(R.id.recipe_image) }

    private val recipeCard: CardView by lazy { view.findViewById(R.id.recipe_card) }

    fun bind(recipeItem: RecipeListItem.RecipeItem) {
        letter.text = recipeItem.title[0].toString()
        title.text = recipeItem.title
        description.text = recipeItem.description
        image.load(recipeItem.imageUrl) {
            placeholder(R.drawable.ic_android_placeholder)
            error(R.drawable.ic_android_placeholder)
        }

        recipeCard.setOnClickListener {
            recipeListener.onRecipeClick(recipeItem.id)
        }
    }
}