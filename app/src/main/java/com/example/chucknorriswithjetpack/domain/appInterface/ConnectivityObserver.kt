package com.example.chucknorriswithjetpack.domain.appInterface

import kotlinx.coroutines.flow.Flow


interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>

    enum class NetworkStatus {
        Available, Unavailable, Loosing, Lost
    }
}