package com.example.chucknorriswithjetpack.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        JokeEntity::class,
        JokeCategoriesEntity::class
    ],
    version = 1
)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun provideRandomDao(): JokeDao
}