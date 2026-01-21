package ru.otus.cookbook.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import com.bumptech.glide.Glide

class RecipeViewHolder(view: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(view) {
    private val root = view.findViewById<ConstraintLayout>(R.id.vh_recipe_item)
    private val letter = view.findViewById<TextView>(R.id.vh_recipe_letter)
    private val title = view.findViewById<TextView>(R.id.vh_recipe_title)
    private val description = view.findViewById<TextView>(R.id.vh_recipe_description)
    private val image = view.findViewById<ImageView>(R.id.vh_recipe_image)

    fun bind(item: RecipeListItem.RecipeItem) {

        letter.text = item.title.first().toString()
        title.text = item.title
        description.text = item.description
        Glide.with(root.context)
            .load(item.imageUrl)
            .centerCrop()
            .circleCrop()
            .into(image)

        root.setOnClickListener { itemClickListener.itemClicked(item.id) }
    }
}