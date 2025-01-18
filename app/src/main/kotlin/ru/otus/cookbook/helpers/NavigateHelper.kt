package ru.otus.cookbook.helpers

import android.annotation.SuppressLint
import androidx.navigation.NavController
import ru.otus.cookbook.ui.CookbookFragmentDirections

object NavigateHelper {

    @SuppressLint("StaticFieldLeak")
    private var _navController: NavController? = null

    fun setNavController(navController: NavController) {
        _navController = navController
    }

    fun showMoreAboutRecipe(id: Int) {
        val action = CookbookFragmentDirections.Companion.actionCookbookFragmentToRecipeFragment(id)
        _navController?.navigate(action)
    }

    fun showAlertDialogOnRecipeFragment(id: Int) {

    _navController?.navigate("alertDialogFragment")
    }
}

