package com.example.chucknorriswithjetpack.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chucknorriswithjetpack.databinding.DialogCategoryListBinding
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.presentation.adapter.CategoryListAdapter
import com.example.chucknorriswithjetpack.presentation.main.JokeCategoryState
import com.example.chucknorriswithjetpack.presentation.main.JokeUiState
import com.example.chucknorriswithjetpack.presentation.main.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListDialog : DialogFragment() {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    lateinit var binding: DialogCategoryListBinding
    private lateinit var catListAdapter: CategoryListAdapter

    private var categoryListener: ((text: String) -> Unit)? = null

    fun setCategoryListener(listener: (text: String) -> Unit) {
        categoryListener = listener
    }

    companion object {
        const val TAG = "CategoryListDialog"
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCategoryListBinding.inflate(layoutInflater)
        setAdapter()
        lifecycleScope.launch {
            mainFragmentViewModel.jokeCategories.collect { jokeCategory ->
                when (jokeCategory) {
                    is JokeCategoryState.Error -> Unit
                    JokeCategoryState.Loading -> Unit
                    is JokeCategoryState.Success -> {
                        val categoriesList = jokeCategory.joke
                        categoriesList?.let {
                            catListAdapter.submitList(it)
                        }
                    }
                }
            }
        }
        mainFragmentViewModel.getJokeCategories()
        return binding.root
    }

    private fun setAdapter() {
        val layoutManagerR = LinearLayoutManager(requireContext())
        catListAdapter = CategoryListAdapter()
        binding.categoryRecyclerView.apply {
            adapter = catListAdapter
            layoutManager = layoutManagerR
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    layoutManagerR.orientation
                )
            )
        }
        catListAdapter.callback = object : CategoryListAdapter.ItemClick {
            override fun onItemClick(jokeCategories: JokeCategories) {
                lifecycleScope.launch {
                    mainFragmentViewModel.getJokeFromCategory(jokeCategories.name)
                    mainFragmentViewModel.randomJoke.collect { jokeUiState ->
                        when (jokeUiState) {
                            is JokeUiState.Error -> Snackbar.make(
                                binding.root,
                                "An error occurred",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            JokeUiState.Loading -> Unit
                            is JokeUiState.Success -> {
                                categoryListener?.let { category ->
                                    category(jokeUiState.joke.joke)
                                    delay(500)
                                    dismiss()

                                }
                            }
                        }

                    }
                }
            }

        }
    }
}