package com.example.moviescleanarchitectureex.di

import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class MoviesRepositoryModule {
    @Binds
    abstract fun provideMoviesRepository(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository
}