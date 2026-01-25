package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.cookbook.R
import kotlin.getValue

class DeleteRecipeDialog: DialogFragment() {

    private val recipeArg by navArgs<DeleteRecipeDialogArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recipeTitle = recipeArg.recipeTitle
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.Delete))
            .setMessage(getString(R.string.Choice, recipeTitle))
            .setPositiveButton(getString(R.string.Ok)) { _, _ ->
                dismiss()
                setDialogResult(true)
            }
            .setNegativeButton(getString(R.string.Cancel)) { _, _ ->
                dismiss()
                setDialogResult(false)
            }
            .create()
    }

    private fun setDialogResult(result: Boolean) {
        val navController = findNavController()
        navController.previousBackStackEntry?.savedStateHandle?.set(
            DIALOG_RESULT,
            result
        )
    }

    companion object {
        const val DIALOG_RESULT = "dialogRes"
    }
}