package com.example.chucknorriswithjetpack.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJokeInDb(jokeModel: JokeEntity): Long

    @Query("SELECT * FROM jokeTable")
    suspend fun getLastRandomJoke(): JokeEntity

    @Query("DELETE FROM jokeTable")
    suspend fun clearJokeDb()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categoryList: List<JokeCategoriesEntity>)

    @Query("SELECT * from jokeCategories")
    suspend fun getCategories(): List<JokeCategoriesEntity>

    @Query("DELETE FROM jokeCategories")
    suspend fun clearCategoriesDb()

}
