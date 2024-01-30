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
    private val context: Context
) {
    private var view: MoviesView? = null
    private var state: MoviesState? = null
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

    private fun renderState(state: MoviesState) {
        this.state = state
        this.view?.render(state)
    }
    fun attachView(view: MoviesView?) {
        this.view = view
        state?.let { view?.render(it) }
    }

    fun detachView() {
        this.view = null
    }

    fun searchDebounce(changedText : String) {
        if (lastSearchText == changedText) {
            return
        }

        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }



    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            view?.render(MoviesState.Loading)

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
                                renderState(
                                    MoviesState.Error(
                                        message = context.getString(R.string.something_went_wrong)
                                    )
                                )
                            }
                            movies.isEmpty() -> {
                                renderState(
                                    MoviesState.Empty(
                                        message = context.getString(R.string.nothing_found)
                                    )
                                )
                            }
                            else -> {
                                renderState(
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