package com.example.moviescleanarchitectureex.di

import android.content.Context
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.presentation.about.AboutFragment
import com.example.moviescleanarchitectureex.presentation.about.AboutViewModel
import com.example.moviescleanarchitectureex.presentation.poster.PosterFragment
import com.example.moviescleanarchitectureex.ui.moviescast.MoviesCastFragment
import dagger.BindsInstance
import dagger.Component


@Component(modules = [AppModule::class])
interface AppComponent {
       // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(moviesSearchViewModel: MoviesSearchViewModel)
    fun inject(aboutViewModel: AboutViewModel)
    fun inject(posterFragment: PosterFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(moviesCastFragment: MoviesCastFragment)
}