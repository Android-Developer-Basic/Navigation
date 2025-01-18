package ru.otus.cookbook.helpers

import android.annotation.SuppressLint
import androidx.navigation.NavController
import ru.otus.cookbook.ui.CookbookFragmentDirections
import ru.otus.cookbook.ui.RecipeFragmentDirections

object NavigateHelper {

    @SuppressLint("StaticFieldLeak")
    private var _navController: NavController? = null

    fun setNavController(navController: NavController) {
        _navController = navController
    }

    fun showMoreAboutRecipe(id: Int) {
        val action =
            CookbookFragmentDirections.actionCookbookFragmentToRecipeFragment(recipeId = id)
        _navController?.navigate(action)
    }

    fun showAlertDialogOnRecipeFragment(recipeName: String) {
        val action =
            RecipeFragmentDirections.actionRecipeFragmentToAlertDialogDialogFragment(recipeName)
        _navController?.navigate(action)
    }
}

