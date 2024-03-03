package com.example.moviescleanarchitectureex.presentation.poster

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.moviescleanarchitectureex.R

class PosterPresenter(
    private val view: PosterView,
    private val imageUrl: String,
) {
    fun onCreate() {
        view.setupPosterImage(imageUrl)
    }
}