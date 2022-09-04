package com.example.chucknorriswithjetpack.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokeTable")
class JokeEntity(
    @PrimaryKey(autoGenerate = true)
    val pk: Int? = null,
    val icon_url: String,
    val id: String,
    val url: String,
    val value: String
)