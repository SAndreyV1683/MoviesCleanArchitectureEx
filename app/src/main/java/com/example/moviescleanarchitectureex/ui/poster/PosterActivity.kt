package com.example.moviescleanarchitectureex.ui.poster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviescleanarchitectureex.util.Creator
import com.example.moviescleanarchitectureex.R

class PosterActivity : AppCompatActivity() {

    private val posterController = Creator.providePosterController(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        posterController.onCreate()
    }
}