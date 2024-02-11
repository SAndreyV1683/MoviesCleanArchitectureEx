package com.example.moviescleanarchitectureex.di

import android.content.Context
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.ui.movies.MoviesActivity
import dagger.BindsInstance
import dagger.Component


@Component(modules = [MoviesRepositoryModule::class, RetrofitClientModule::class])
interface AppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(moviesSearchViewModel: MoviesSearchViewModel)
}