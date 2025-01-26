package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.cookbook.R


class DeleteRecipeDialogFragment : DialogFragment() {

    companion object {
        const val CONFIRMATION_RESULT = "confirmation_result"
//        const val IS_DELETED = "isDeleted"
//        const val DELETE_RECIPE_RESULT = "DELETE_RECIPE_RESULT"
    }

    private val args: DeleteRecipeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val recipeTitle = args.recipeTitle
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_recipe_title))
            .setMessage(getString(R.string.delete_recipe_message, recipeTitle))
            .setPositiveButton(R.string.ok) { _, _ ->
//                setFragmentResult(DELETE_RECIPE_RESULT, Bundle().apply {
//                    putBoolean(IS_DELETED, true)
//                })
                    dismiss()
                    setResult(true)
                }
                .setNegativeButton(R.string.cancel, { _, _ ->
                    dismiss()
                    setResult(false)
                }
            )
            .create()
    }



//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
//        AlertDialog.Builder(requireContext())
//            .setMessage(message)
//            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
//                dismiss()
//                setResult(true)
//            }
//            .setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
//                dismiss()
//                setResult(false)
//            }
//            .create()

    private fun setResult(result: Boolean) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            CONFIRMATION_RESULT,
            result
        )
        findNavController().popBackStack()
    }

}