package ru.otus.cookbook.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val categoryName = view.findViewById<TextView>(R.id.category_name_tv)

    fun bind(item: RecipeListItem.CategoryItem) {
        categoryName.text = item.name
    }
}