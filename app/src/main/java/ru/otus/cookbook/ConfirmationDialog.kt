package ru.otus.cookbook

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.otus.cookbook.ui.RESULT_KEY
import kotlin.getValue

class ConfirmationDialog : DialogFragment() {

    private val args: ConfirmationDialogArgs by navArgs()
    private val recipeTitle: String get() = args.recipeTitle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Удалить рецепт $recipeTitle?")
            .setPositiveButton("Да") { _, _ ->
                setResult(true)
            }
            .setNegativeButton("Нет") { _, _ ->
                setResult(false)
            }
            .create()

    private fun setResult(result: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(RESULT_KEY, result)
    }
}