package com.example.moviescleanarchitectureex.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.ui.models.MoviesState
import com.example.moviescleanarchitectureex.util.Creator

class MoviesSearchPresenter(
    val view: MoviesView,
    private val context: Context
) {

    private val moviesInteractor = Creator.provideMoviesInteractor(context)
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private var lastSearchText: String? = null
    private val movies = ArrayList<Movie>()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchRequest(newSearchText)
    }

    fun searchDebounce(changedText : String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }



    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            view.render(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, message: String?) {
                    handler.post {
                        if (foundMovies != null) {
                            // Обновляем список на экране
                            movies.clear()
                            movies.addAll(foundMovies)
                        }
                        when  {
                            message != null -> {
                                view.render(
                                    MoviesState.Error(
                                        message = context.getString(R.string.something_went_wrong)
                                    )
                                )
                            }
                            movies.isEmpty() -> {
                                view.render(
                                    MoviesState.Empty(
                                        message = context.getString(R.string.nothing_found)
                                    )
                                )
                            }
                            else -> {
                                view.render(
                                    MoviesState.Content(
                                        movies = movies
                                    )
                                )
                            }
                        }
                    }
                }
            })
        }
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }
}