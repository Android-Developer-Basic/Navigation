package ru.otus.cookbook.cook_recycler_view

import android.annotation.SuppressLint
import androidx.navigation.NavController
import ru.otus.cookbook.ui.CookbookFragmentDirections


object RecipeTouchHelper {

    @SuppressLint("StaticFieldLeak")
    private var _navController: NavController? = null

    fun setNavController(navController: NavController) {
        _navController = navController
    }

    fun onRcViewItemClick(id: Int) {
        val action = CookbookFragmentDirections.Companion.actionCookbookFragmentToRecipeFragment(id)
        _navController?.navigate(action)

    }
}

