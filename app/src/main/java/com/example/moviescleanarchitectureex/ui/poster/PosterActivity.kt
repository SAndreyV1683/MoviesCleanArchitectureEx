package com.example.moviescleanarchitectureex.ui.poster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.moviescleanarchitectureex.util.Creator
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.presentation.poster.PosterPresenter
import com.example.moviescleanarchitectureex.presentation.poster.PosterView

class PosterActivity : AppCompatActivity(), PosterView {

    private lateinit var posterPresenter: PosterPresenter

    private lateinit var poster: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Мы не можем создать PosterPresenter раньше,
        // потому что нам нужен imageUrl, который
        // станет доступен только после super.onCreate
        val imageUrl = intent.extras?.getString("poster", "") ?: ""
        posterPresenter = Creator.providePosterPresenter(this, imageUrl)

        setContentView(R.layout.activity_poster)
        poster = findViewById(R.id.poster)

        posterPresenter.onCreate()
    }

    override fun setupPosterImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .into(poster)
    }

}