package ru.otus.cookbook.ui

import android.annotation.SuppressLint
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
import ru.otus.cookbook.R
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.otus.cookbook.data.Recipe
import ru.otus.cookbook.databinding.FragmentRecipeBinding
import androidx.activity.OnBackPressedCallback

class RecipeFragment : Fragment() {

    // Получение ID рецепта через SafeArgs
    private val recipeId: Int get() = RecipeFragmentArgs.fromBundle(requireArguments()).recipeId

    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)
    private val model: RecipeFragmentViewModel by viewModels(
        extrasProducer = {
            androidx.lifecycle.viewmodel.MutableCreationExtras(defaultViewModelCreationExtras)
                .apply {
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
        setupToolbar()
        setupDeleteButton()
        setupDeleteResult()
        setupBackPressHandler()
        viewLifecycleOwner.lifecycleScope.launch {
            model.recipe
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::displayRecipe)
        }
    }

    private fun setupToolbar() = binding.withBinding {
        toolbar.setupWithNavController(findNavController())
    }

    private fun setupDeleteButton() = binding.withBinding {
        deleteButton.setOnClickListener {
            // Навигация в диалог подтверждения удаления с передачей названия рецепта
            val action = RecipeFragmentDirections.actionRecipeToDeleteDialog(getTitle())
            findNavController().navigate(action)
        }
    }

    private fun setupDeleteResult() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.recipeFragment)

        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                // Проверка результата подтверждения удаления
                if (navBackStackEntry.savedStateHandle.contains(DeleteRecipeDialogFragment.CONFIRMATION_RESULT)) {
                    val confirmed =
                        navBackStackEntry.savedStateHandle.get<Boolean>(DeleteRecipeDialogFragment.CONFIRMATION_RESULT) == true

                    if (confirmed) {
                        deleteRecipe()
                        // Возврат к списку рецептов после удаления
                        findNavController().popBackStack()
                    }

                    // Удаление результата из SavedStateHandle
                    navBackStackEntry.savedStateHandle.remove<Boolean>(
                        DeleteRecipeDialogFragment.CONFIRMATION_RESULT
                    )
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

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Возвращаемся к списку рецептов
                    findNavController().popBackStack()
                }
            })
    }

    private fun getTitle(): String {
        return model.recipe.value.title
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        // Заголовок рецепта
        recipeTitle.text = recipe.title

        // Категория рецепта
        recipeCategory.text = recipe.category.name

        // Загрузка изображения через Glide
        Glide.with(this@RecipeFragment)
            .load(recipe.imageUrl)
            .into(recipeImage)

        recipeImage.setImageResource(android.R.drawable.ic_menu_gallery)

        // Описание рецепта
        recipeDescription.text = recipe.description

        // Настройка списка шагов приготовления
        stepsLabel.text = getString(R.string.title_recipe_steps)
        stepsRecycler.layoutManager = LinearLayoutManager(requireContext())
        stepsRecycler.adapter = StepsAdapter(recipe.steps)
    }

    private fun deleteRecipe() {
        model.delete()
    }
}

private class StepsAdapter(private val steps: List<String>) :
    RecyclerView.Adapter<StepsAdapter.StepViewHolder>() {

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = com.google.android.material.textview.MaterialTextView(parent.context).apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.MarginLayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT
            ).apply {
                val margin = parent.context.resources.getDimensionPixelSize(
                    ru.otus.cookbook.R.dimen.margin_small
                )
                setMargins(margin, margin, margin, margin)
            }

            setTextAppearance(
                parent.context,
                com.google.android.material.R.attr.textAppearanceBody1
            )
            setPadding(
                parent.context.resources.getDimensionPixelSize(ru.otus.cookbook.R.dimen.margin),
                0,
                parent.context.resources.getDimensionPixelSize(ru.otus.cookbook.R.dimen.margin),
                0
            )
        }
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position], position + 1)
    }

    override fun getItemCount(): Int = steps.size

    class StepViewHolder(private val textView: com.google.android.material.textview.MaterialTextView) :
        RecyclerView.ViewHolder(textView) {
        @SuppressLint("SetTextI18n")
        fun bind(step: String, number: Int) {
            textView.text = "$number. $step"
        }
    }
}