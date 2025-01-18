package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding
import ru.otus.cookbook.helpers.NavigateHelper

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
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }

        val materialToolbar = view.findViewById<MaterialToolbar>(R.id.titleToolbar)
        materialToolbar.setOnMenuItemClickListener {
            NavigateHelper.showAlertDialogOnRecipeFragment(getTitle(), this)
            true
        }
    }

    /**
     * Use to get recipe title and pass to confirmation dialog
     */
    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) {
        view?.let {
            val imageRecipe = it.findViewById<ImageView>(R.id.imageRecipeOnAbout)
            Glide.with(this)
                .load(recipe.imageUrl)
                .into(imageRecipe)

            it.findViewById<TextView>(R.id.nameRecipe).text = recipe.title
            it.findViewById<TextView>(R.id.typeRecipe).text = recipe.category.name
            it.findViewById<TextView>(R.id.descriptionRecipe).text = recipe.description

            Glide.with(this)
                .load(recipe.imageUrl)
                .into(imageRecipe)
        }
    }

    fun onAlertDialogDelete() {
        deleteRecipe()
    }

    private fun deleteRecipe() {
        model.delete()
    }
}