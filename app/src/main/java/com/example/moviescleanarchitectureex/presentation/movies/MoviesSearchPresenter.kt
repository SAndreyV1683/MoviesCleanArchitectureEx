package com.example.moviescleanarchitectureex.presentation.movies

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.Movie
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
            view.showPlaceholderMessage(false)
            view.showMoviesList(false)
            view.showProgressBar(true)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, message: String?) {
                    handler.post {
                        view.showProgressBar(false)
                        if (foundMovies != null) {

                            // Обновляем список на экране
                            movies.clear()
                            movies.addAll(foundMovies)
                            view.updateMoviesList(movies)
                            view.showMoviesList(true)
                        }
                        if (message != null) {
                            showMessage(context.getString(R.string.something_went_wrong), message)
                        } else if (movies.isEmpty()) {
                            showMessage(context.getString(R.string.nothing_found), "")
                        } else {
                            hideMessage()
                        }
                    }
                }
            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            // Заменили работу с элементами UI на
            // вызовы методов интерфейса
            view.showPlaceholderMessage(true)
            movies.clear()
            view.updateMoviesList(movies)
            // Добавили вызов метода MoviesView
            view.changePlaceholderText(text)
            if (additionalMessage.isNotEmpty()) {
                view.showMessage(additionalMessage)
            }
        } else {
            // Заменили работу с элементами UI на
            // вызовы методов интерфейса
            view.showPlaceholderMessage(false)
        }
    }

    private fun hideMessage() {
        // Заменили работу с элементами UI на
        // вызовы методов интерфейса
        view.showPlaceholderMessage(false)
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }
}