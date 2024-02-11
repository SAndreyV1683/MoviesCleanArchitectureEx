package com.example.moviescleanarchitectureex.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.ui.models.MoviesState
import com.example.moviescleanarchitectureex.ui.movies.SingleLiveEvent
import com.example.moviescleanarchitectureex.util.Creator
import javax.inject.Inject

class MoviesSearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MoviesSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
    init {
        (application as MoviesApplication).appComponent.inject(this)
    }

    @Inject
    lateinit var moviesInteractor: MoviesInteractor //= Creator.provideMoviesInteractor(getApplication())
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private val showToast = SingleLiveEvent<String>()
    fun observeShowToast(): LiveData<String> = showToast

    private var latestSearchText: String? = null

    fun toggleFavorite(movie: Movie) {
        if (movie.inFavorite) {
            moviesInteractor.removeMovieFromFavorites(movie)
        } else {
            moviesInteractor.addMovieToFavorites(movie)
        }
        updateMovieContent(movie.id, movie.copy(inFavorite = !movie.inFavorite))
    }

    private val mediatorStateLiveData = MediatorLiveData<MoviesState>().also { liveData ->
        // 1
        liveData.addSource(stateLiveData) { movieState ->
            liveData.value = when (movieState) {
                // 2
                is MoviesState.Content -> MoviesState.Content(movieState.movies.sortedByDescending { it.inFavorite })
                is MoviesState.Empty -> movieState
                is MoviesState.Error -> movieState
                is MoviesState.Loading -> movieState
            }
        }
    }



    private fun updateMovieContent(movieId: String, newMovie: Movie) {
        val currentState = stateLiveData.value

        // 2
        if (currentState is MoviesState.Content) {
            // 3
            val movieIndex = currentState.movies.indexOfFirst { it.id == movieId }

            // 4
            if (movieIndex != -1) {
                // 5
                stateLiveData.value = MoviesState.Content(
                    currentState.movies.toMutableList().also {
                        it[movieIndex] = newMovie
                    }
                )
            }
        }
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            moviesInteractor.searchMovies(newSearchText, object : MoviesInteractor.MoviesConsumer {
                override fun consume(foundMovies: List<Movie>?, message: String?) {
                    val movies = mutableListOf<Movie>()
                    if (foundMovies != null) {
                        movies.addAll(foundMovies)
                    }

                    when {
                        message != null -> {
                            renderState(
                                MoviesState.Error(
                                    message = getApplication<Application>().getString(R.string.something_went_wrong),
                                )
                            )
                            showToast.postValue(message)
                        }

                        movies.isEmpty() -> {
                            renderState(
                                MoviesState.Empty(
                                    message = getApplication<Application>().getString(R.string.nothing_found),
                                )
                            )
                        }

                        else -> {
                            renderState(
                                MoviesState.Content(
                                    movies = movies,
                                )
                            )
                        }
                    }

                }
            })
        }
    }

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

}