package com.example.chucknorriswithjetpack.domain.model

//class to be used in our presesntation layer
data class RandomJokeModel(
    val iconUrl: String,
    val jokeId: String,
    val jokeUrl: String,
    val joke: String
)