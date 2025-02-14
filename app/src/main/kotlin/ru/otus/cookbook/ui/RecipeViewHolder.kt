package ru.otus.cookbook.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem

class RecipeViewHolder(view: View, private val itemClickListener: ItemClickListener) : RecyclerView.ViewHolder(view) {
    private val root = view.findViewById<ConstraintLayout>(R.id.recipe_item_cl)
    private val letter = view.findViewById<TextView>(R.id.letter_tv)
    private val title = view.findViewById<TextView>(R.id.title_tv)
    private val description = view.findViewById<TextView>(R.id.description_tv)
    private val image = view.findViewById<ImageView>(R.id.item_iv)

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