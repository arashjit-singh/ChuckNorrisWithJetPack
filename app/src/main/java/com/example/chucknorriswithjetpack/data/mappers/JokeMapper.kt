package com.example.chucknorriswithjetpack.data.mappers

import com.example.chucknorriswithjetpack.data.database.JokeCategoriesEntity
import com.example.chucknorriswithjetpack.data.database.JokeEntity
import com.example.chucknorriswithjetpack.data.remote.dto.JokeDto
import com.example.chucknorriswithjetpack.data.remote.dto.Result
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel

fun JokeEntity.toJokeModel(): RandomJokeModel {
    return RandomJokeModel(
        iconUrl = icon_url,
        jokeId = id,
        jokeUrl = url,
        joke = value
    )
}

fun RandomJokeModel.toJokeEntity(): JokeEntity {
    return JokeEntity(
        icon_url = iconUrl,
        id = jokeId,
        url = jokeUrl,
        value = joke
    )
}

fun JokeDto.toRandomJoke(): RandomJokeModel {
    return RandomJokeModel(
        iconUrl = icon_url ?: "",
        jokeId = id ?: "",
        jokeUrl = url ?: "",
        joke = value ?: "",
    )
}

fun JokeDto.toJokeEntity(): JokeEntity {
    return JokeEntity(
        icon_url = icon_url ?: "",
        id = id ?: "",
        url = url ?: "",
        value = value ?: "",
    )
}

fun String.toJokeCategoryEntity(): JokeCategoriesEntity {
    return JokeCategoriesEntity(
        categoryName = this
    )
}

fun JokeCategoriesEntity.toJokeCategory(): JokeCategories {
    return JokeCategories(
        name = categoryName,
        id = pk ?: 0
    )
}

fun Result.toJokeModel(): RandomJokeModel {
    return RandomJokeModel(
        iconUrl = icon_url ?: "",
        jokeId = id ?: "",
        jokeUrl = url ?: "",
        joke = value ?: "",
    )
}