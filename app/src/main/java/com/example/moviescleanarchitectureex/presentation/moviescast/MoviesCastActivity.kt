package com.example.moviescleanarchitectureex.presentation.moviescast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviescleanarchitectureex.appComponent
import com.example.moviescleanarchitectureex.databinding.ActivityMoviesCastBinding
import javax.inject.Inject

class MoviesCastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesCastBinding
    private lateinit var movieId: String
    private val moviesCastViewModel: MoviesCastViewModel by viewModels {
        factory.create(movieId)
    }
    @Inject
    lateinit var factory: MoviesCastViewModel.MovieCastViewModelFactory.Factory
    private val adapter = MoviesCastAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        movieId = intent.getStringExtra(ARGS_MOVIE_ID) ?: ""
        this.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Привязываем адаптер и LayoutManager к RecyclerView
        binding.movieCastRecyclerView.adapter = adapter
        binding.movieCastRecyclerView.layoutManager = LinearLayoutManager(this)

        // Наблюдаем за UiState из ViewModel
        moviesCastViewModel.observeState().observe(this) {
            // В зависимости от UiState экрана показываем
            // разные состояния экрана
            when (it) {
                is MoviesCastState.Content -> showContent(it)
                is MoviesCastState.Error -> showError(it)
                is MoviesCastState.Loading -> showLoading()
            }
        }

    }

    private fun showLoading() {
        binding.contentContainer.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.progressBar.isVisible = true
    }

    private fun showError(state: MoviesCastState.Error) {
        binding.contentContainer.isVisible = false
        binding.progressBar.isVisible = false

        binding.errorMessageTextView.isVisible = true
        binding.errorMessageTextView.text = state.message
    }

    private fun showContent(state: MoviesCastState.Content) {
        binding.progressBar.isVisible = false
        binding.errorMessageTextView.isVisible = false

        binding.contentContainer.isVisible = true
        binding.movieTitle.text = state.fullTitle

        // Просто объединяем всех участников
        // в единый список и отображаем
        adapter.items = state.items
        adapter.notifyDataSetChanged()
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