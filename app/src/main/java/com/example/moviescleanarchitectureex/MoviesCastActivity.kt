package com.example.moviescleanarchitectureex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviescleanarchitectureex.databinding.ActivityMoviesCastBinding

class MoviesCastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesCastBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        private const val ARGS_MOVIE_ID = "movie_id"

        fun newInstance(context: Context, movieId: String): Intent {
            return Intent(context, MoviesCastActivity::class.java).apply {
                putExtra(ARGS_MOVIE_ID, movieId)
            }
        }
    }
}