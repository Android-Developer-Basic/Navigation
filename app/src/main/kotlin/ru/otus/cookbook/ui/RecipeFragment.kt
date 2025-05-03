package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        setupDialogObserver()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    private fun setupDialogObserver() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            DialogFragmentDelete.DIALOG_RESULT_KEY
        )?.observe(viewLifecycleOwner) { result ->
            if (result) {
                deleteRecipe()
            }
        }
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun setupAppBar() {
        binding.withBinding {
            recipeToolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_recipe_to_cookbook)
            }
            recipeToolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_recipe -> {
                        val navController = findNavController()
                        val action = RecipeFragmentDirections.actionRecipeToDialogDelete(getTitle())
                        navController.navigate(action)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun displayRecipe(recipe: Recipe) {
        binding.withBinding {
            txtRecipeName.text = recipe.title
            txtRecipeDescription.text = recipe.description
            txtRecipeSteps.text =
                recipe.steps.mapIndexed { index, step -> "${index + 1}. $step" }.joinToString("\n")
            Glide.with(root.context).load(recipe.imageUrl).into(imgRecipeImageBig)
            recipeToolbar.title = recipe.title
        }
    }

    private fun deleteRecipe() {
        model.delete()
        findNavController().navigate(R.id.action_dialog_delete_to_cookbook)
    }
}