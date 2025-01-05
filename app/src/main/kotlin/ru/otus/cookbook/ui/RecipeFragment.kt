package ru.otus.cookbook.ui

import android.os.Bundle
import android.util.Log
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
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupAlertResult()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }
    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }


        private fun setupAppBar() = binding.withBinding {
            topAppBar.setNavigationOnClickListener {
                close()
            }
            topAppBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {
                        deleteRecipe()
                        true
                    }
                    else -> false
                }
            }
        }

        private fun close() {
            findNavController().popBackStack()
        }

        /**
         * Sets up alert dialog for delete result.
         * https://developer.android.com/guide/navigation/use-graph/programmatic#returning_a_result
         */
        private fun setupAlertResult() {
            val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

            val observer = object : DefaultLifecycleObserver {
                override fun onResume(owner: LifecycleOwner) {
                    if (navBackStackEntry.savedStateHandle.contains(DeleteConfirmationFragment.CONFIRMATION_RESULT)) {
                        if (true == navBackStackEntry.savedStateHandle.get<Boolean>(DeleteConfirmationFragment.CONFIRMATION_RESULT)) {
                            Log.d(TAG, "Deleting recipe $recipeId")
                            model.delete()
                            close()
                        }
                        navBackStackEntry.savedStateHandle.remove<Boolean>(DeleteConfirmationFragment.CONFIRMATION_RESULT)
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

        private fun displayRecipe(recipe: Recipe) = binding.withBinding {
            Glide.with(requireContext())
                .load(recipe.imageUrl)
                .into(imageViewRecipe)
            title.text = recipe.title
            steps.text = recipe.steps.joinToString("\n")
        }

        private fun deleteRecipe() {
            Log.d(TAG, "Deleting recipe $recipeId")
            findNavController().navigate(
                RecipeFragmentDirections.actionRecipeFragmentToDeleteConfirmation(getTitle())
            )
        }

        companion object {
            private const val TAG = "RecipeFragment"
        }
    }