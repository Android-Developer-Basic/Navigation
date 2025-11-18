package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }

    // Адаптер для списка рецептов
    private val adapter = RecipeListAdapter { recipeId ->
        // Навигация к экрану детальной информации о рецепте с использованием Safe Args
        val action = CookbookFragmentDirections.actionCookbookToRecipe(recipeId)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentCookbookBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipeList
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onRecipeListUpdated)
        }
    }

    /**
     * Настройка RecyclerView: подключение адаптера и LayoutManager
     */
    private fun setupRecyclerView() = binding.withBinding {
        recipes.adapter = adapter
    }

    /**
     * Обновление списка рецептов в адаптере при изменении данных
     */
    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        adapter.submitList(recipeList)
    }
}