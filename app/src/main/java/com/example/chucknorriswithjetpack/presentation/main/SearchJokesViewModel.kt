package com.example.chucknorriswithjetpack.presentation.main

import androidx.lifecycle.ViewModel

class SearchJokesViewModel :
    ViewModel() {

   /* private val _searchJoke =
        MutableStateFlow<SearchJokeUiState>(SearchJokeUiState.Empty)

    val searchJoke = _searchJoke.asStateFlow()*/


    /*fun searchForAJoke(query: String) {
        viewModelScope.launch {
            searchRepository.searchForAJoke(query).collect { searchJokeValue ->
                _searchJoke.value = SearchJokeUiState.Success(searchJokeValue)
            }
        }
    }*/

   /* // Represents different states for the screen
    sealed class SearchJokeUiState {
        data class Success(val joke: SearchJokeModel) : SearchJokeUiState()
        data class Error(val message: String) : SearchJokeUiState()
        object Loading : SearchJokeUiState()
        object Empty : SearchJokeUiState()
    }*/
}