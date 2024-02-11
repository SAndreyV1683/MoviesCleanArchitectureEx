package com.example.moviescleanarchitectureex

import android.app.Application
import com.example.moviescleanarchitectureex.di.AppComponent
import com.example.moviescleanarchitectureex.di.DaggerAppComponent


import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel


open class MoviesApplication : Application() {
    var moviesSearchViewModel : MoviesSearchViewModel? = null
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph
        DaggerAppComponent.factory().create(this)
    }
    override fun onCreate() {
        super.onCreate()
    }
}