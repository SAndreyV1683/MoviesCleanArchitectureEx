package com.example.moviescleanarchitectureex

import android.app.Application
import com.example.moviescleanarchitectureex.presentation.movies.MoviesSearchViewModel

class MoviesApplication : Application() {
    var moviesSearchViewModel : MoviesSearchViewModel? = null
}