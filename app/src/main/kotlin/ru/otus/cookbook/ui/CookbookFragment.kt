package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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
    private val viewModel: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }
    private val recipesAdapter = RecipesAdapter(::openRecipe)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentCookbookBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecycler()
        observeRecipes()
    }

    private fun setupToolbar() {
        val activity = activity as? AppCompatActivity ?: return

        binding.withBinding {
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun setupRecycler() {
        binding.withBinding {
            categoryRecyclerView.adapter = recipesAdapter
        }
    }

    private fun observeRecipes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::updateRecipes)
        }
    }

    private fun openRecipe(recipeId: Int) {
        findNavController().navigate(
            CookbookFragmentDirections.actionCookbookFragmentToRecipeFragment(recipeId)
        )
    }

    private fun updateRecipes(recipeList: List<RecipeListItem>) {
        recipesAdapter.submitList(recipeList)
    }
}