package ru.otus.cookbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.otus.cookbook.HasRecipeRepository
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.data.RecipeRepository
import ru.otus.cookbook.data.getRecipeList

/**
 * View model for the cookbook list screen.
 * Subscribe [recipeList] for the recipe list.
 */
class CookbookFragmentViewModel(private val repository: RecipeRepository) : ViewModel() {

    /**
     * Recipe list to display in the UI.
     */
    val recipeList: StateFlow<List<RecipeListItem>> = flow {
        emitAll(repository.getRecipeList(viewModelScope))
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    companion object {
        /**
         * Factory for creating [CookbookFragmentViewModel].
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CookbookFragmentViewModel(
                    (get(APPLICATION_KEY) as HasRecipeRepository).recipeRepository
                )
            }
        }
    }
}
