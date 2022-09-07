package com.example.chucknorriswithjetpack.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chucknorriswithjetpack.R
import com.example.chucknorriswithjetpack.databinding.DialogCategoryListBinding
import com.example.chucknorriswithjetpack.presentation.adapter.JokeListAdapter
import com.example.chucknorriswithjetpack.presentation.main.MainFragmentViewModel
import com.example.chucknorriswithjetpack.presentation.main.SearchJokeState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserSearchJokeListDialog : DialogFragment() {

    companion object {
        const val TAG = "UserSearchJokeListDialog"
    }

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding: DialogCategoryListBinding
    private lateinit var jokeListAdapter: JokeListAdapter
    private var searchText = ""

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun setAdapter() {
        val layoutManagerR = LinearLayoutManager(requireContext())
        binding.categoryRecyclerView.apply {
            jokeListAdapter = JokeListAdapter()
            adapter = jokeListAdapter
            layoutManager = layoutManagerR
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    layoutManagerR.orientation
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchText = arguments?.let {
            it.getString("searchText", "")
        } ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryListBinding.inflate(layoutInflater)
        setAdapter()
        lifecycleScope.launch {
            mainFragmentViewModel.searchJoke.collect { searchJoke ->
                when (searchJoke) {
                    is SearchJokeState.Success -> {
                        val jokes = searchJoke.joke
                        jokes.let {
                            if (it.isNotEmpty())
                                jokeListAdapter.submitList(it)
                            else {
                                Snackbar.make(
                                    requireActivity().findViewById(R.id.rootView),
                                    "No Joke found for $searchText",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                dismiss()
                            }
                        }
                    }
                    is SearchJokeState.Error -> {
                        binding.progressBar.isVisible = false
                        Snackbar.make(
                            binding.root,
                            searchJoke.message,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is SearchJokeState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                }

            }
        }
        mainFragmentViewModel.searchForJoke(searchText)
        return binding.root
    }


}