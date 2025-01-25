package ru.otus.cookbook.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.otus.cookbook.R


class DeleteDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dialogText = view.findViewById<TextView>(R.id.dialogText)

        dialogText.text = getString(
            R.string.are_you_sure_you_wand_to_delete,
            DeleteDialogFragmentArgs.fromBundle(requireArguments()).title
        )
        view.findViewById<TextView>(R.id.ok).setOnClickListener {
            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set(CONFIRMATION_RESULT, true)
            navController.popBackStack()
        }
        view.findViewById<TextView>(R.id.cancel).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val CONFIRMATION_RESULT = "result"
    }
}