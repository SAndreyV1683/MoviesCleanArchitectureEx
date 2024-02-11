package com.example.moviescleanarchitectureex.util

import android.content.Context
import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.impl.MoviesInteractorImpl
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.presentation.poster.PosterPresenter
import com.example.moviescleanarchitectureex.presentation.poster.PosterView

object Creator {
    private fun getMoviesRepository(context: Context): MoviesRepository {
        return MoviesRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context)
        )
    }

    fun provideMoviesInteractor(context: Context): MoviesInteractor {
        return MoviesInteractorImpl(getMoviesRepository(context))
    }

    fun providePosterPresenter(
        posterView: PosterView,
        imageUrl: String
    ): PosterPresenter {
        return PosterPresenter(posterView, imageUrl)
    }
}