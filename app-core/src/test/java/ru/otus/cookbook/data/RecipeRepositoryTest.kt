package ru.otus.cookbook.data

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.otus.cookbook.mockRecipes
import ru.otus.cookbook.newRecipe

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepositoryTest {

    private lateinit var repository: RecipeRepository

    @Before
    fun init() {
        repository = RecipeRepository(mockRecipes)
    }

    @Test
    fun `returns a list of recipes`() = runTest(UnconfinedTestDispatcher()) {
        val recipes = repository.getRecipes().first()

        val expected = listOf(
            mockRecipes[0],
            mockRecipes[1],
            mockRecipes[2]
        )

        assertEquals(expected, recipes)
    }

    @Test
    fun `deletes recipe`() = runTest(UnconfinedTestDispatcher()) {
        val recipes = backgroundScope.async {
            repository.getRecipes().take(2).toList()
        }

        repository.deleteRecipe(2)

        val expected = listOf(
            listOf(
                mockRecipes[0],
                mockRecipes[1],
                mockRecipes[2]
            ),
            listOf(
                mockRecipes[0],
                mockRecipes[2]
            )
        )

        assertEquals(expected, recipes.await())
    }

    @Test
    fun `adds recipe`() = runTest(UnconfinedTestDispatcher()) {
        val recipes = backgroundScope.async {
            repository.getRecipes().take(2).toList()
        }

        repository.addRecipe(newRecipe)

        val expected = listOf(
            listOf(
                mockRecipes[0],
                mockRecipes[1],
                mockRecipes[2]
            ),
            listOf(
                mockRecipes[0],
                mockRecipes[1],
                mockRecipes[2],
                newRecipe.copy(id = 4)
            )
        )

        assertEquals(expected, recipes.await())
    }
}
