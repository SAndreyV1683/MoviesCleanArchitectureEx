package com.example.moviescleanarchitectureex.di

import com.example.moviescleanarchitectureex.data.NetworkClient
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import dagger.Binds
import dagger.Module

@Module
abstract class RetrofitClientModule {
    @Binds
    abstract fun provideRetrofitClient(retrofitNetworkClient: RetrofitNetworkClient): NetworkClient
}