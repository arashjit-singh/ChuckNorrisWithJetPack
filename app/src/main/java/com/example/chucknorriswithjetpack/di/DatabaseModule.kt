package com.example.chucknorriswithjetpack.di

import android.content.Context
import androidx.room.Room
import com.example.chucknorriswithjetpack.data.database.JokeDatabase
import com.example.chucknorriswithjetpack.data.database.JokeDao
import com.example.chucknorriswithjetpack.domain.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseBuilder(@ApplicationContext context: Context): JokeDatabase {
        return Room.databaseBuilder(
            context, JokeDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideJokeDao(db: JokeDatabase): JokeDao {
        return db.provideRandomDao()
    }


}