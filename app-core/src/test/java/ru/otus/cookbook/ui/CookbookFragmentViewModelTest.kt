package ru.otus.cookbook.ui

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ru.otus.cookbook.data.RecipeRepository
import ru.otus.cookbook.data.toRecipeListItems
import ru.otus.cookbook.mockRecipes

@ExperimentalCoroutinesApi
class CookbookFragmentViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CookbookFragmentViewModel

    @Before
    fun init() {
        Dispatchers.setMain(dispatcher)
        viewModel = CookbookFragmentViewModel(RecipeRepository(mockRecipes))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `gets default recipe list`() = runTest(dispatcher) {
        val expected = mockRecipes.toRecipeListItems()

        val actual = viewModel.recipeList.first()

        assertEquals(expected, actual)
    }
}
