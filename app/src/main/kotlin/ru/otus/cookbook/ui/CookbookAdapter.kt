package ru.otus.cookbook.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.otus.cookbook.data.Recipe

class CookbookAdapter(private val categories: List<Recipe>) :
    RecyclerView.Adapter<CookbookAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(itemView: View) : ViewHolder(itemView) {}

    inner class RecipeViewHolder(itemView: View) : ViewHolder(itemView) {}

}