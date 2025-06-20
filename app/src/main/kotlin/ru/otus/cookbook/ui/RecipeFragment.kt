package ru.otus.cookbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding
import ru.otus.cookbook.ui.RecipeFragmentArgs

class RecipeFragment : Fragment() {

    private val args by navArgs<RecipeFragmentArgs>()
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
        binding.withBinding {
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupAppBar()
        observeLiveData()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    fun observeLiveData(){
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            DeleteRecipeDialogFragment.DIALOG_RESULT_KEY
        )?.observe(viewLifecycleOwner) {
            if (it) {
                deleteRecipe()
                navController.navigateUp()
            }
        }
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) {
        binding.withBinding {
            textViewTittle.text = recipe.title
            textViewDescription.text = recipe.description
            textViewRecipe.text = recipe.steps.mapIndexed { index, step ->  "${index + 1}. $step"}
                .joinToString("\n")
            Glide.with(root)
                .load(recipe.imageUrl)
                .centerCrop()
                .into(imageViewRecipe)
        }
    }

    private fun setupAppBar() {
        binding.withBinding {
            (activity as? AppCompatActivity)?.supportActionBar?.title = getTitle()
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            imageViewDelete.setOnClickListener {
                findNavController().navigate(
                            RecipeFragmentDirections.actionRecipeFragmentToDeleteRecipeDialogFragment(
                                getTitle()
                            )
                        )
            }
        }
    }

    private fun deleteRecipe() {
        model.delete()
    }
}