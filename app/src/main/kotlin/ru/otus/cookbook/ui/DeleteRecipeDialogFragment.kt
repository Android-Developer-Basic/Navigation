package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.otus.cookbook.R

class DeleteRecipeDialogFragment : DialogFragment() {

    companion object {
        const val DELETE_RESULT = "delete_result"
    }

    private val args: DeleteRecipeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete)
            .setMessage(getString(R.string.delete_recipe_message, args.title))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                dismiss()
                setResult(true)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                dismiss()
                setResult(false)
            }
            .create()
    }

    private fun setResult(confirmed: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            DELETE_RESULT,
            confirmed
        )
        findNavController().popBackStack()
    }
}
