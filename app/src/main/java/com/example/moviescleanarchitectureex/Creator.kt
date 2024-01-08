package com.example.moviescleanarchitectureex

import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.impl.MoviesInteractorImpl

object Creator {
    private fun getMoviesRepository(): MoviesRepository {
        return MoviesRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository())
    }
}