package com.example.chucknorriswithjetpack.data.remote.dto

data class SearchJoke(
    val result: List<Result>,
    val total: Int
)