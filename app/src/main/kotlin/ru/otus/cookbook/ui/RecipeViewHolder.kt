package ru.otus.cookbook.ui

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class RecipeViewHolder (
    private val view: View,
    private val listener: Listener,
) : RecyclerView.ViewHolder(view) {
    private val root: ConstraintLayout by lazy { view.findViewById(R.id.recipeItem) }
    private val letter: TextView by lazy { view.findViewById(R.id.letter) }
    private val title: TextView by lazy { view.findViewById(R.id.recipeTitle) }
    private val desc: TextView by lazy { view.findViewById(R.id.recipeDesc) }

    fun bind(item: RecipeListItem.RecipeItem) {
        letter.text = item.title.substring(0, 1)
        title.text = item.title
        desc.text = item.description
        root.setOnClickListener { listener.onItemClicked(item.id) }
    }
}

class CategoryViewHolder (
    private val view: View,
) : RecyclerView.ViewHolder(view) {
    private val title: TextView by lazy { view.findViewById(R.id.categoryTitle) }

    fun bind(item: RecipeListItem.CategoryItem) {
        title.text = item.name
    }
}