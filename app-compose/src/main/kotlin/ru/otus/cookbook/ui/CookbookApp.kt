package ru.otus.cookbook.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import ru.otus.cookbook.HasRecipeRepository
import ru.otus.cookbook.ui.navigation.CookbookRoute
import ru.otus.cookbook.ui.navigation.DeleteRecipeRoute
import ru.otus.cookbook.ui.navigation.RecipeRoute
import ru.otus.cookbook.ui.theme.CookbookBackground

@Composable
fun CookbookApp() {
    val backStack = rememberNavBackStack(CookbookRoute)
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
    val repository = (LocalContext.current.applicationContext as HasRecipeRepository).recipeRepository

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CookbookBackground
    ) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(dialogStrategy),

            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<CookbookRoute> {
                    CookbookScreen(
                        onRecipeClick = { recipeId ->
                            backStack.add(RecipeRoute(recipeId))
                        }
                    )
                }

                entry<RecipeRoute> { route ->
                    RecipeScreen(
                        recipeId = route.recipeId,
                        onBack = { backStack.removeLastOrNull() },
                        onDeleteClick = { title ->
                            backStack.add(
                                DeleteRecipeRoute(
                                    title = title,
                                    recipeId = route.recipeId
                                )
                            )
                        }
                    )
                }

                entry<DeleteRecipeRoute>(
                    metadata = DialogSceneStrategy.dialog(DialogProperties())
                ) { route ->
                    DeleteRecipeDialog(
                        title = route.title,
                        onConfirm = {
                            repository.deleteRecipe(route.recipeId)
                            backStack.removeLastOrNull() // dialog
                            backStack.removeLastOrNull() // detail → list
                        },
                        onDismiss = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}
