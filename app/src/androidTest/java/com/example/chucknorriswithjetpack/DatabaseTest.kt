package com.example.chucknorriswithjetpack

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.chucknorriswithjetpack.data.database.JokeDao
import com.example.chucknorriswithjetpack.data.database.JokeDatabase
import com.example.chucknorriswithjetpack.data.database.ProvideDummyJokeModel
import com.example.chucknorriswithjetpack.data.mappers.toJokeEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    lateinit var dao: JokeDao

    lateinit var database: JokeDatabase

    @Before
    fun createDB() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, JokeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.provideRandomDao()!!
    }


    @Test
    fun testJokeInsert() = runBlocking {
        val randomJoke = ProvideDummyJokeModel.getDummyModel().toJokeEntity()
        dao.insertJokeInDb(randomJoke)
        val myJoke = dao.getLastRandomJoke()
        assertEquals(myJoke.id, randomJoke.id)
    }

    @After
    fun closeDb() {
        database.close()
    }
}