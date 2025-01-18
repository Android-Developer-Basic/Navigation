package ru.otus.cookbook.helpers

import android.annotation.SuppressLint
import androidx.navigation.NavController
import ru.otus.cookbook.ui.CookbookFragmentDirections
import ru.otus.cookbook.ui.RecipeFragment
import ru.otus.cookbook.ui.RecipeFragmentDirections
import ru.otus.cookbook.R

object NavigateHelper {

    @SuppressLint("StaticFieldLeak")
    private var _navController: NavController? = null
    private var _recipeFragment: RecipeFragment? = null

    fun setNavController(navController: NavController) {
        _navController = navController
    }

    fun showMoreAboutRecipe(id: Int) {
        val action =
            CookbookFragmentDirections.actionCookbookFragmentToRecipeFragment(recipeId = id)
        _navController?.navigate(action)
    }

    fun showAlertDialogOnRecipeFragment(recipeName: String, recipeFragment: RecipeFragment) {
        _recipeFragment = recipeFragment
        val action =
            RecipeFragmentDirections.actionRecipeFragmentToAlertDialogDialogFragment(recipeName)
        _navController?.navigate(action)
    }

    fun onDelete() {
        _recipeFragment?.onAlertDialogDelete()
        _navController?.popBackStack(R.id.cookbookFragment, false)
        _recipeFragment = null
    }
}

