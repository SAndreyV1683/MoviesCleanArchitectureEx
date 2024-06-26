package com.example.moviescleanarchitectureex.di

import android.content.Context
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel
import com.example.moviescleanarchitectureex.ui.about.AboutFragment
import com.example.moviescleanarchitectureex.presentation.about.AboutViewModel
import com.example.moviescleanarchitectureex.presentation.poster.PosterFragment
import com.example.moviescleanarchitectureex.ui.history.HistoryFragment
import com.example.moviescleanarchitectureex.ui.moviescast.MoviesCastFragment
import com.example.moviescleanarchitectureex.ui.names.NamesFragment
import com.example.moviescleanarchitectureex.ui.root.RootActivity
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
    fun inject(rootActivity: RootActivity)
    fun inject(namesFragment: NamesFragment)
    fun inject(historyFragment: HistoryFragment)
}