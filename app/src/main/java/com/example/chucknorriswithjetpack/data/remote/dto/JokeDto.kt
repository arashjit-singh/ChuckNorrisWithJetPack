package com.example.chucknorriswithjetpack.data.remote.dto

import com.squareup.moshi.Json

class JokeDto(
    @field:Json(name = "icon_url") val icon_url: String?,
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "value") val value: String?
)