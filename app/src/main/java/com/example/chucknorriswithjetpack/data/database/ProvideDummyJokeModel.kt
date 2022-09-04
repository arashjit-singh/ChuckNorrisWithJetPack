package com.example.chucknorriswithjetpack.data.database

import com.example.chucknorriswithjetpack.domain.model.RandomJokeModel

class ProvideDummyJokeModel {

    companion object {
        fun getDummyModel() =
            RandomJokeModel("", "", "", "")
    }

}