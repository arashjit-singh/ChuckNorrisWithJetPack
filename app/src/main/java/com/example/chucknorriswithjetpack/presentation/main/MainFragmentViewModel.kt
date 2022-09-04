package com.example.chucknorriswithjetpack.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel
import com.example.chucknorriswithjetpack.domain.repository.JokeRepository
import com.example.chucknorriswithjetpack.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: JokeRepository) :
    ViewModel() {

    private val _randomJoke =
        MutableStateFlow<JokeUiState>(JokeUiState.Empty)

    val randomJoke = _randomJoke.asStateFlow()

    private val _jokeCategories = MutableStateFlow<JokeCategoryState>(JokeCategoryState.Empty)
    val jokeCategories = _jokeCategories.asStateFlow()

    private val _searchJoke = MutableStateFlow<SearchJokeState>(SearchJokeState.Empty)
    val searchJoke = _searchJoke.asStateFlow()

    fun getRandomJoke(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            repository.getRandomJoke(fetchFromRemote).collect { result ->
                when (result) {
                    is Resource.Error ->
                        _randomJoke.value =
                            JokeUiState.Error(result.message.toString())
                    is Resource.Loading ->
                        _randomJoke.value = JokeUiState.Loading
                    is Resource.Success -> {
                        result?.data?.let {
                            _randomJoke.value = JokeUiState.Success(it)
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
                        _jokeCategories.value =
                            JokeCategoryState.Error(jokeCategory.message.toString())
                    is Resource.Loading ->
                        _jokeCategories.value = JokeCategoryState.Loading
                    is Resource.Success -> {
                        jokeCategory?.data?.let {
                            _jokeCategories.value = JokeCategoryState.Success(it)
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
                    is Resource.Error -> _searchJoke.value =
                        SearchJokeState.Error(jokeSearch.message.toString())
                    is Resource.Loading -> SearchJokeState.Loading
                    is Resource.Success ->
                        jokeSearch?.data?.let {
                            _searchJoke.value = SearchJokeState.Success(it)
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
    object Empty : JokeUiState()
}

sealed class JokeCategoryState {
    data class Success(val joke: List<JokeCategories>) : JokeCategoryState()
    data class Error(val message: String) : JokeCategoryState()
    object Loading : JokeCategoryState()
    object Empty : JokeCategoryState()
}

sealed class SearchJokeState {
    data class Success(val joke: List<RandomJokeModel>) : SearchJokeState()
    data class Error(val message: String) : SearchJokeState()
    object Loading : SearchJokeState()
    object Empty : SearchJokeState()
}
