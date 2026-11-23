package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val args: RecipeFragmentArgs by navArgs()

    private val recipeId: Int get() = args.recipeId

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
        setupToolbar()
        observeDeleteResult()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    private fun setupToolbar() = binding.withBinding {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_delete) {
                openDeleteDialog()
                true
            } else {
                false
            }
        }
    }

    /**
     * Same pattern as the webinar / Navigation docs:
     * read dialog result from SavedStateHandle when this back stack entry resumes.
     */
    private fun observeDeleteResult() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (!navBackStackEntry.savedStateHandle.contains(DeleteRecipeDialogFragment.DELETE_RESULT)) {
                    return
                }
                val confirmed = navBackStackEntry.savedStateHandle.get<Boolean>(
                    DeleteRecipeDialogFragment.DELETE_RESULT
                )
                navBackStackEntry.savedStateHandle.remove<Boolean>(
                    DeleteRecipeDialogFragment.DELETE_RESULT
                )
                if (confirmed == true) {
                    deleteRecipe()
                    findNavController().popBackStack(R.id.cookbookFragment, false)
                }
            }
        }

        navBackStackEntry.lifecycle.addObserver(observer)
        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun openDeleteDialog() {
        findNavController().navigate(
            RecipeFragmentDirections.actionRecipeFragmentToDeleteRecipeDialogFragment(getTitle())
        )
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        toolbar.title = recipe.title
        title.text = recipe.title
        description.text = recipe.description
        steps.text = recipe.steps
            .mapIndexed { index, step -> "${index + 1}. $step" }
            .joinToString(separator = "\n\n")
        image.load(recipe.imageUrl) {
            crossfade(true)
            scale(Scale.FILL)
            placeholder(R.drawable.ic_recipe_detail_placeholder)
            error(R.drawable.ic_recipe_detail_placeholder)
        }
    }

    private fun deleteRecipe() {
        model.delete()
    }
}
