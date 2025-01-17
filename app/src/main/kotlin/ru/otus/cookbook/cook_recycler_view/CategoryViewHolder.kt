package ru.otus.cookbook.cook_recycler_view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class CategoryViewHolder(private val categoryView: View) : RecyclerView.ViewHolder(categoryView) {
    private val categoryText: TextView by lazy { categoryView.findViewById(R.id.categoryText) }

    fun bind(categoryData: RecipeListItem.CategoryItem) {
        categoryText.text = categoryData.name
    }
}
