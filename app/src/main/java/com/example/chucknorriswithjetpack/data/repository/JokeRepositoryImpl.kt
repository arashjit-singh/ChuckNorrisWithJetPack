package com.example.chucknorriswithjetpack.data.repository

import com.example.chucknorriswithjetpack.data.database.JokeDao
import com.example.chucknorriswithjetpack.data.database.JokeDatabase
import com.example.chucknorriswithjetpack.data.mappers.toJokeCategory
import com.example.chucknorriswithjetpack.data.mappers.toJokeCategoryEntity
import com.example.chucknorriswithjetpack.data.mappers.toJokeEntity
import com.example.chucknorriswithjetpack.data.mappers.toJokeModel
import com.example.chucknorriswithjetpack.data.remote.ApiService
import com.example.chucknorriswithjetpack.domain.model.JokeCategories
import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel
import com.example.chucknorriswithjetpack.domain.repository.JokeRepository
import com.example.chucknorriswithjetpack.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val db: JokeDatabase,
    private val dao: JokeDao
) : JokeRepository {
    override suspend fun getRandomJoke(fetchFromRemote: Boolean): Flow<Resource<RandomJokeModel>> {
        return flow {
            //start showing progress bar
            emit(Resource.Loading(true))
            //search for data in local db
            if (!fetchFromRemote) {
                val localJoke = dao.getLastRandomJoke()
                emit(Resource.Loading(false))
                emit(
                    Resource.Success(
                        data = localJoke.toJokeModel()
                    )
                )
                return@flow
            }

            val randomJoke = try {
                api.getRandomJoke()
            } catch (e: IOException) {
                //eg something wrong with parsing
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                //eg like wrong URL
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            randomJoke?.let { joke ->
                //Insert joke in local db
                dao.clearJokeDb()
                dao.insertJokeInDb(joke.toJokeEntity())
                val joke = dao.getLastRandomJoke()
                emit(Resource.Loading(false))
                emit(
                    Resource.Success(
                        data = joke.toJokeModel()
                    )
                )
            }
        }


    }

    override suspend fun getJokeCategories(): Flow<Resource<List<JokeCategories>>> {
        return flow {
            emit(Resource.Loading(true))
            val jokeCategoriesFromDb = dao.getCategories()
            if (jokeCategoriesFromDb.isNotEmpty()) {
                emit(Resource.Loading(false))
                val categoryData = jokeCategoriesFromDb.map {
                    it.toJokeCategory()
                }
                emit(
                    Resource.Success(
                        data = categoryData
                    )
                )
            } else {
                val jokeCategories = try {
                    api.getJokeCategories().string()
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                }

                jokeCategories?.let { response ->

                    val listCat = response.trim()
                        .splitToSequence(',').filter {
                            it.isNotEmpty()
                        }.map {
                            it.toJokeCategoryEntity()
                        }.toList()

                    listCat?.let {
                        dao.clearCategoriesDb()
                        dao.insertCategories(it)
                        val categoriesList = dao.getCategories()
                        val categoryData = categoriesList.map { entity ->
                            entity.toJokeCategory()
                        }
                        emit(
                            Resource.Success(
                                data = categoryData
                            )
                        )
                        emit(Resource.Loading(false))
                    }

                }
            }
        }
    }

    override suspend fun searchForJoke(query: String): Flow<Resource<List<RandomJokeModel>>> {
        return flow {
            try {
                emit(Resource.Loading(true))
                val jokes = try {
                    api.searchForJokes(query)
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Resource.Error("Couldn't load data"))
                    null
                }

                jokes?.let { jokes ->
                    val data = jokes.result.map {
                        it.toJokeModel()
                    }
                    emit(
                        Resource.Success(
                            data = data
                        )
                    )
                    emit(Resource.Loading(false))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

        }
    }
}