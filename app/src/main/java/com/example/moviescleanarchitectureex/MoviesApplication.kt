package com.example.moviescleanarchitectureex

import android.app.Application
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchPresenter

class MoviesApplication : Application() {
    var moviesSearchPresenter : MoviesSearchPresenter? = null
}