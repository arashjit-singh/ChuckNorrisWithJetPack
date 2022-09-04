package com.example.chucknorriswithjetpack.domain.repository

import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel
import com.example.chucknorriswithjetpack.util.Resource
import kotlinx.coroutines.flow.Flow

interface JokeRepository {
    suspend fun getRandomJoke(fetchFromRemote: Boolean): Flow<Resource<RandomJokeModel>>
    suspend fun getJokeCategories(): Flow<Resource<List<JokeCategories>>>
    suspend fun searchForJoke(queryParam: String): Flow<Resource<List<RandomJokeModel>>>
}