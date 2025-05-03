package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.FragmentCookbookBinding

class CookbookFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }

    private val adapter = CookbookAdapter(
        { id ->
            val navController = findNavController()
            val action = CookbookFragmentDirections.actionCookbookToRecipe(id)
            navController.navigate(action)
        }
    )

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
        setupAppBar()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onRecipeListUpdated)
        }
    }

    private fun setupAppBar() {
        binding.withBinding {
            cookbookToolbar.setNavigationOnClickListener {
                Toast.makeText(requireContext(), "Close application?", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun setupRecyclerView() = binding.withBinding {
        recViewCategory.adapter = adapter
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        adapter.submitList(recipeList)
    }
}