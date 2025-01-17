package ru.otus.cookbook.cook_recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class RecipeViewHolder(private val recipeView: View) : RecyclerView.ViewHolder(recipeView) {

    private val title: TextView by lazy { recipeView.findViewById(R.id.titleRecipe) }
    private val description: TextView by lazy { recipeView.findViewById(R.id.titleDescription) }
    private val imageRecipe: ImageView by lazy { recipeView.findViewById(R.id.imageRecipe) }


    fun bind(recipeData: RecipeListItem.RecipeItem) {
        recipeView.setOnClickListener { RecipeTouchHelper().onRcViewItemClick(recipeData.id) }

        title.text = recipeData.title
        description.text = recipeData.description

        Glide.with(recipeView.context)
            .load(recipeData.imageUrl)
            .into(imageRecipe)
    }
}
