package ru.otus.cookbook.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.otus.cookbook.R

/**
 * Диалог подтверждения удаления рецепта
 */
class DeleteRecipeDialogFragment : DialogFragment() {

    companion object {
        const val CONFIRMATION_RESULT = "confirmation_result"
    }

    // Получение названия рецепта через Safe Args
    private val recipeTitle: String get() = DeleteRecipeDialogFragmentArgs.fromBundle(requireArguments()).recipeTitle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(getString(R.string.delete_recipe_confirmation, recipeTitle))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                dismiss()
                setResult(true)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                dismiss()
                setResult(false)
            }
            .create()

    private fun setResult(result: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(CONFIRMATION_RESULT, result)
        findNavController().popBackStack()
    }
}

