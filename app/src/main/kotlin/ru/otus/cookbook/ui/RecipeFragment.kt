package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
        savedInstanceState: Bundle?,
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
            detailRecipeMt.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            detailRecipeMt.setOnMenuItemClickListener {
                val action = RecipeFragmentDirections.actionRecipeFragmentToDeleteDialogFragment(getTitle())
                findNavController().navigate(action)
                true
            }
        }

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME &&
                navBackStackEntry.savedStateHandle.contains(DeleteDialogFragment.DELETE_CONFIRMATION_RESULT)
            ) {
                if (navBackStackEntry
                        .savedStateHandle.get<Boolean>(DeleteDialogFragment.DELETE_CONFIRMATION_RESULT) == true
                ) {
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

    private fun displayRecipe(recipe: Recipe) {
        binding.withBinding {
            detailRecipeMt.title = recipe.title
            detailRecipeTitleTv.text = recipe.title
            detailRecipeDescriptionTv.text = recipe.description
            detailRecipeStepsTv.text = recipe.steps.joinToString(".")
            Glide.with(this@RecipeFragment)
                .load(recipe.imageUrl)
                .centerCrop()
                .into(detailRecipeImageIv)
        }
    }

    private fun deleteRecipe() {
        model.delete()
    }
}