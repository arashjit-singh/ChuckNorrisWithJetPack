package com.example.chucknorriswithjetpack.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.chucknorriswithjetpack.R
import com.example.chucknorriswithjetpack.databinding.FragmentMainBinding
import com.example.chucknorriswithjetpack.presentation.dialog.CategoryListDialog
import com.example.chucknorriswithjetpack.presentation.dialog.InfoDialog
import com.example.chucknorriswithjetpack.presentation.dialog.SearchJokeDialog
import com.example.chucknorriswithjetpack.presentation.dialog.UserSearchJokeListDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment(), View.OnClickListener {

    private val mainFragmentViewModel: MainFragmentViewModel by viewModels()
    private lateinit var binding: FragmentMainBinding
    private var isDialogVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.app_name)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                launch {
                    mainFragmentViewModel.randomJoke.collect { jokeUiState ->
                        when (jokeUiState) {
                            is JokeUiState.Success -> {
                                binding.progressBar.isVisible = false
                                context?.let {
                                    InfoDialog().apply {
                                        setYesListener {
                                            isDialogVisible = false
                                        }
                                        val args = Bundle()
                                        args.putString("title", "Random Joke")
                                        args.putString("msg", jokeUiState.joke.joke)
                                        arguments = args
                                    }.show(
                                        parentFragmentManager, InfoDialog.TAG
                                    )
                                    isDialogVisible = true
                                }
                            }
                            is JokeUiState.Error -> {
                                binding.progressBar.isVisible = false
                                Snackbar.make(
                                    binding.root,
                                    jokeUiState.message,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            is JokeUiState.Loading -> {
                                binding.progressBar.isVisible = true
                            }
                            is JokeUiState.Empty -> Unit
                        }
                    }

                }
            }
        }
        binding.buttonRandomJoke.setOnClickListener(this)
        binding.buttonSearchJoke.setOnClickListener(this)
        binding.buttonCategories.setOnClickListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val infoDialog = parentFragmentManager.findFragmentByTag(InfoDialog.TAG)
            infoDialog?.let {
                val dialog = it as InfoDialog
                if (!isDialogVisible)
                    dialog.dismiss()
            }

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.buttonRandomJoke -> {
                mainFragmentViewModel.getRandomJoke(true)
            }
            R.id.buttonSearchJoke -> {
                val dialog = SearchJokeDialog().apply {
                    isCancelable = true
                    setSearchListener { searchText ->
                        UserSearchJokeListDialog().apply {
                            val args = Bundle()
                            args.putString("searchText", searchText)
                            arguments = args
                        }.show(parentFragmentManager, UserSearchJokeListDialog.TAG)
                    }
                }
                dialog.show(
                    parentFragmentManager, SearchJokeDialog.TAG
                )

            }
            R.id.buttonCategories -> {
                val dialog = CategoryListDialog().apply {
                    isCancelable = true
                }
                dialog.show(
                    parentFragmentManager, CategoryListDialog.TAG
                )
            }
        }
    }


}


