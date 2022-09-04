package com.example.chucknorriswithjetpack.di

import com.example.chucknorriswithjetpack.data.repository.JokeRepositoryImpl
import com.example.chucknorriswithjetpack.domain.repository.JokeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: JokeRepositoryImpl
    ): JokeRepository

}