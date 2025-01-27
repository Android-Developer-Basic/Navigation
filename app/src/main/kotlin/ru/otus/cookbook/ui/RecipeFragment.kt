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
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val args: RecipeFragmentArgs by navArgs()
    private val recipeId: Int get() = args.recipeId  // Теперь у вас есть recipeId // ("Use Safe Args to get the recipe ID: https://developer.android.com/guide/navigation/use-graph/pass-data#Safe-args")

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
            btnBack.setOnClickListener({
                findNavController().navigateUp()
            })
            btnDelete.setOnClickListener({
                val argTitle = getTitle()
                val action = RecipeFragmentDirections
                    .actionRecipeFragmentToDeleteRecipeDialogFragment(argTitle)
                findNavController().navigate(action)
            })
        }
        setupAlertResult()
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        // Display the recipe
        tvTitle.text = recipe.title
        // Устанавливаем заголовок рецепта
        detailTitle.text = recipe.title
        // Устанавливаем категорию рецепта
        detailShortDescription.text = recipe.category.name
        // Устанавливаем описание рецепта
        detailFullDescription.text = recipe.description
        // Загружаем изображение рецепта
        Glide.with(this@RecipeFragment)
            .load(recipe.imageUrl)
            .transform(NormalizeAndRoundedCornersTransformation(
                requireContext(),16, 0,
                NormalizeAndRoundedCornersTransformation.CornerType.ALL))
            .into(detailImage)
    }

    private fun deleteRecipe() {
        model.delete()
    }


    /**
     * Sets up alert dialog for delete result.
     * https://developer.android.com/guide/navigation/use-graph/programmatic#returning_a_result
     */
    private fun setupAlertResult() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                if (navBackStackEntry.savedStateHandle.contains(DeleteRecipeDialogFragment.CONFIRMATION_RESULT)) {
                    if (true == navBackStackEntry.savedStateHandle.get<Boolean>(DeleteRecipeDialogFragment.CONFIRMATION_RESULT)) {
                        deleteRecipe()
                        findNavController().popBackStack()
                    }
                    navBackStackEntry.savedStateHandle.remove<Boolean>(DeleteRecipeDialogFragment.CONFIRMATION_RESULT)
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
}