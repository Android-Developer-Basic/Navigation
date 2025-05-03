package ru.otus.cookbook.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.otus.cookbook.R

class DialogFragmentDelete : DialogFragment() {
    private val title: String get() = DialogFragmentDeleteArgs.fromBundle(requireArguments()).title

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        requireContext().let {
            val navController = findNavController()
            AlertDialog.Builder(it)

                .setTitle(getString(R.string.delete_dialog_title, title))
                .setMessage(getString(R.string.delete_dialog_message, title))
                .setPositiveButton(getString(R.string.delete_dialog_positive_button)) { _, _ ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        DIALOG_RESULT_KEY,
                        true
                    )
                }
                .setNegativeButton(getString(R.string.delete_dialog_negative_button)) { _, _ ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        DIALOG_RESULT_KEY,
                        false
                    )
                }
                .create()
        }

    companion object {
        const val DIALOG_RESULT_KEY = "deleteResult"
    }

}

