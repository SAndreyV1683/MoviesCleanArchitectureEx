package com.example.moviescleanarchitectureex

import android.app.Application
import android.content.Context
import com.example.moviescleanarchitectureex.di.AppComponent
import com.example.moviescleanarchitectureex.di.DaggerAppComponent


import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel


open class MoviesApplication : Application() {



    lateinit var appComponent: AppComponent
        private set

    companion object {
        lateinit var INSTANCE: MoviesApplication
    }
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        INSTANCE = this
    }
}


val Context.appComponent: AppComponent
    get() = when (this) {
        is MoviesApplication -> appComponent
        else -> applicationContext.appComponent
    }
