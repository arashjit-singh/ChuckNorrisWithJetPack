package com.example.chucknorriswithjetpack.data.remote

import com.example.chucknorriswithjetpack.data.remote.dto.JokeDto
import com.example.chucknorriswithjetpack.data.remote.dto.SearchJoke
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random")
    suspend fun getRandomJoke(): JokeDto

    @GET("categories")
    suspend fun getJokeCategories(): ResponseBody

    @GET("search")
    suspend fun searchForJokes(
        @Query("query") query: String
    ): SearchJoke

}