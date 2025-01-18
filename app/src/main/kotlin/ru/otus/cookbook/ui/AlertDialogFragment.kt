package ru.otus.cookbook.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.otus.cookbook.helpers.NavigateHelper

class AlertDialogFragment : DialogFragment() {
    private val args: AlertDialogFragmentArgs by navArgs()
    private val recipeName: String get() = args.recipeName

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = activity?.let { MaterialAlertDialogBuilder(it) }

        builder?.apply {
            setTitle("Delete")
            setMessage("Are you sure you want to delete $recipeName ?")
            setNegativeButton("Cancel") { _, _ ->
                return@setNegativeButton
            }
            setPositiveButton("OK") { _, _ ->

            }
        }
        return builder?.create()!!
    }
}