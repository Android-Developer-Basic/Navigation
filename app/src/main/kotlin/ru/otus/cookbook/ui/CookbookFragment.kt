package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.FragmentCookbookBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CookbookFragment : Fragment(), ItemClickListener {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }
    private val adapter: CookBookAdapter by lazy { CookBookAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentCookbookBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onRecipeListUpdated)
        }
        binding.withBinding {
            cookbookToolbar.setNavigationOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(getString(R.string.close_app_message))
                    .setPositiveButton(R.string.yes_btn) { _, _ ->
                        requireActivity().finishAndRemoveTask()
                    }
                    .setNegativeButton(R.string.no_btn, null)
                    .show()
            }
        }
    }

    private fun setupRecyclerView() = binding.withBinding {
        cookbookList.adapter = adapter
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        adapter.submitList(recipeList)
    }

    override fun itemClicked(id: Int) {
        val action = CookbookFragmentDirections.actionCookbookToRecipe(id)
        findNavController().navigate(action)
    }
}