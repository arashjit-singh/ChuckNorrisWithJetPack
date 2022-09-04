package com.example.chucknorriswithjetpack.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chucknorriswithjetpack.R
import com.example.chucknorriswithjetpack.databinding.FragmentSearchJokesBinding
import com.example.chucknorriswithjetpack.domain.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchJokesFragment : Fragment() {

    val viewModel: SearchJokesViewModel by viewModels()
    lateinit var binding: FragmentSearchJokesBinding
    private val args: SearchJokesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchJokesBinding.inflate(layoutInflater, container, false)
        Log.i(TAG, "onCreateView: $args")

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
            it.title = "Search for ${args.searchText}"
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    /*viewModel.searchJoke.collect { searchJoke ->
                        when (searchJoke) {
                            is SearchJokesViewModel.SearchJokeUiState.Empty -> {
                                Unit
                            }
                            is SearchJokesViewModel.SearchJokeUiState.Error -> {
                                Unit
                            }
                            is SearchJokesViewModel.SearchJokeUiState.Loading -> {
                                Unit
                            }
                            is SearchJokesViewModel.SearchJokeUiState.Success -> {
                                Unit
                            }
                        }
                    }*/
                }
            }
        }

//        viewModel.searchForAJoke(args.searchText)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }

    }

}