package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.cookbook.R

class DeleteDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(
                getString(
                    R.string.delete_dialog_message,
                    DeleteDialogFragmentArgs.fromBundle(requireArguments()).recipeTitle
                )
            )
            .setPositiveButton(R.string.ok_btn) { _, _ ->
                dismiss()
                setConfirmationResult(true)
            }
            .setNegativeButton(R.string.cancel_btn) { _, _ ->
                dismiss()
                setConfirmationResult(false)
            }
            .create()
    }

    private fun setConfirmationResult(result: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            DELETE_CONFIRMATION_RESULT, result
        )
    }

    companion object {
        const val DELETE_CONFIRMATION_RESULT = "result"
    }

}