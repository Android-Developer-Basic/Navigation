package ru.otus.cookbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.otus.cookbook.R
import ru.otus.cookbook.data.RecipeListItem
import ru.otus.cookbook.databinding.FragmentCookbookBinding

class CookbookFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private val model: CookbookFragmentViewModel by viewModels { CookbookFragmentViewModel.Factory }

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
        setupSearch()

        binding.withBinding {
            btnClose.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.close_application))
                    .setMessage(getString(R.string.are_you_sure_you_want_to_exit))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        val activity = requireActivity()
                        NotificationManagerCompat.from(activity).cancelAll()
                        activity.finishAndRemoveTask()
                    }
                    .setNegativeButton(getString(R.string.no), null)
                    .show()
            }
        }
    }

    private fun performSearch(query: String) {
        // Реализация поиска
        Toast.makeText(requireContext(), getString(R.string.query, query), Toast.LENGTH_SHORT).show()
    }

    private fun setupSearch() = binding.withBinding {

        // Обработчик нажатия на иконку
        searchIcon.setOnClickListener {
            val query = searchInput.text.toString()
            performSearch(query)
        }

        // Обработчик нажатия кнопки "Поиск" на клавиатуре
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchInput.text.toString()
                performSearch(query)
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() = binding.withBinding {
        // Setup RecyclerView
        val adapter = RecipeAdapter(requireContext()) { recipeId ->
            // Handle recipe click, e.g., navigate to RecipeFragment
            val action = CookbookFragmentDirections.actionCookbookFragmentToRecipeFragment(recipeId)
            findNavController().navigate(action)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) = binding.withBinding {
        // Handle recipe list
        (recyclerView.adapter as RecipeAdapter).submitList(recipeList)
    }
}