package com.example.chucknorriswithjetpack.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokeCategories")
class JokeCategoriesEntity(
    @PrimaryKey(autoGenerate = true)
    val pk: Int? = null,
    val categoryName: String
)