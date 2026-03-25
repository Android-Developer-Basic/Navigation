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
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val args by navArgs<RecipeFragmentArgs>()
    private val recipeId get() = args.recipeId

    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)
    private lateinit var navController: NavController

    private val viewModel: RecipeFragmentViewModel by viewModels(
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
    ): View = binding.bind(container, FragmentRecipeBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        setupToolbar()
        setupActions()
        observeDeleteResult()
        observeRecipe()
    }

    private fun setupToolbar() {
        val activity = activity as? AppCompatActivity ?: return

        binding.withBinding {
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = getTitle()
            toolbar.setNavigationOnClickListener { navController.navigateUp() }
        }
    }

    private fun setupActions() {
        binding.withBinding {
            refuseBin.setOnClickListener {
                navController.navigate(
                    RecipeFragmentDirections.actionRecipeFragmentToDeleteRecipeDialogFragment(
                        getTitle()
                    )
                )
            }
        }
    }

    private fun observeDeleteResult() {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getLiveData<Boolean>(DeleteRecipeDialog.DIALOG_RESULT)
            ?.observe(viewLifecycleOwner) { result ->
                if (result) {
                    deleteRecipe()
                    navController.navigateUp()
                }
            }
    }

    private fun observeRecipe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    private fun getTitle(): String = viewModel.recipe.value.title

    private fun displayRecipe(recipe: Recipe) {
        binding.withBinding {
            recipeTitle.text = recipe.title
            recipeDescription.text = recipe.description
            recipeDetail.text = recipe.steps
                .mapIndexed { index, step -> "${index + 1}. $step" }
                .joinToString("\n")

            Glide.with(root)
                .load(recipe.imageUrl)
                .centerCrop()
                .into(recipeImage)
        }
    }

    private fun deleteRecipe() {
        viewModel.delete()
    }
}