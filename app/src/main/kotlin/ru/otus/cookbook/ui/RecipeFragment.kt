package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val recipeId: Int get() = RecipeFragmentArgs.fromBundle(requireArguments()).recipeId

    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)
    private val model: RecipeFragmentViewModel by viewModels(
        extrasProducer = {
            MutableCreationExtras(defaultViewModelCreationExtras).apply {
                set(RecipeFragmentViewModel.RECIPE_ID_KEY, recipeId)
            }
        },
        factoryProducer = { RecipeFragmentViewModel.Factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
        binding.withBinding {
            recipeMenuBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            recipeMenuBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.delete -> {
                        val action = RecipeFragmentDirections.actionRecipeFragmentToDeleteDialogFragment(getTitle())
                        findNavController().navigate(action)
                        true
                    }
                    else -> false
                }
            }
        }

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(DeleteDialogFragment.CONFIRMATION_RESULT)) {
                val isConfirmed = navBackStackEntry.savedStateHandle.get<Boolean>(DeleteDialogFragment.CONFIRMATION_RESULT) ?: false;
                if (isConfirmed) {
                    deleteRecipe()
                    findNavController().popBackStack()
                }
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        title.text = recipe.title
        desc.text = recipe.description
        steps.text = recipe.steps.joinToString("\n")
    }

    private fun deleteRecipe() {
        model.delete()
    }
}