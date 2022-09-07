package com.example.chucknorriswithjetpack.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel
import com.example.chucknorriswithjetpack.domain.repository.JokeRepository
import com.example.chucknorriswithjetpack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: JokeRepository) :
    ViewModel() {

    init {
        Timber.i("MainFragmentViewModel Initialized")
    }

    private val _randomJoke =
        MutableSharedFlow<JokeUiState>()
    private val _jokeCategories = MutableSharedFlow<JokeCategoryState>()
    private val _searchJoke = MutableSharedFlow<SearchJokeState>()

    val randomJoke = _randomJoke.asSharedFlow()
    val jokeCategories = _jokeCategories.asSharedFlow()
    val searchJoke = _searchJoke.asSharedFlow()

    fun getRandomJoke(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            repository.getRandomJoke(fetchFromRemote).collect { result ->
                when (result) {
                    is Resource.Error ->
                        _randomJoke.emit(JokeUiState.Error(result.message.toString()))
                    is Resource.Loading ->
                        _randomJoke.emit(JokeUiState.Loading)
                    is Resource.Success -> {
                        result?.data?.let {
                            _randomJoke.emit(JokeUiState.Success(it))
                        }
                    }
                }
            }
        }
    }

    fun getJokeCategories() {
        viewModelScope.launch {
            repository.getJokeCategories().collect { jokeCategory ->
                when (jokeCategory) {
                    is Resource.Error ->
                        _jokeCategories.emit(JokeCategoryState.Error(jokeCategory.message.toString()))
                    is Resource.Loading ->
                        _jokeCategories.emit(JokeCategoryState.Loading)
                    is Resource.Success -> {
                        jokeCategory?.data?.let {
                            _jokeCategories.emit(JokeCategoryState.Success(it))
                        }
                    }
                }
            }
        }
    }

    fun searchForJoke(query: String) {
        viewModelScope.launch {
            repository.searchForJoke(query).collect { jokeSearch ->
                when (jokeSearch) {
                    is Resource.Error -> _searchJoke.emit(SearchJokeState.Error(jokeSearch.message.toString()))
                    is Resource.Loading -> _searchJoke.emit(SearchJokeState.Loading)
                    is Resource.Success ->
                        jokeSearch?.data?.let {
                            _searchJoke.emit(SearchJokeState.Success(it))
                        }
                }

            }
        }
    }

    fun getJokeFromCategory(category: String) {
        viewModelScope.launch {
            repository.getJokeFromCategory(category).collect { jokeSearch ->
                when (jokeSearch) {
                    is Resource.Error -> _randomJoke.emit(JokeUiState.Error(jokeSearch.message.toString()))
                    is Resource.Loading -> _randomJoke.emit(JokeUiState.Loading)
                    is Resource.Success ->
                        jokeSearch?.data?.let {
                            _randomJoke.emit(JokeUiState.Success(it))
                        }
                }
            }
        }
    }

}

// Represents different states for the screen
sealed class JokeUiState {
    data class Success(val joke: RandomJokeModel) : JokeUiState()
    data class Error(val message: String) : JokeUiState()
    object Loading : JokeUiState()
//    object Empty : JokeUiState()
}

sealed class JokeCategoryState {
    data class Success(val joke: List<JokeCategories>) : JokeCategoryState()
    data class Error(val message: String) : JokeCategoryState()
    object Loading : JokeCategoryState()
//    object Empty : JokeCategoryState()
}

sealed class SearchJokeState {
    data class Success(val joke: List<RandomJokeModel>) : SearchJokeState()
    data class Error(val message: String) : SearchJokeState()
    object Loading : SearchJokeState()
//    object Empty : SearchJokeState()
}
