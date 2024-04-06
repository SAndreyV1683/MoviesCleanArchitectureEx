package com.example.moviescleanarchitectureex.presentation.movies

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.Movie
import com.example.moviescleanarchitectureex.presentation.SingleLiveEvent
import com.example.moviescleanarchitectureex.ui.models.MoviesState
import com.example.moviescleanarchitectureex.util.debounce
import kotlinx.coroutines.launch
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
    lateinit var moviesInteractor: MoviesInteractor
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<MoviesState>()
    fun observeState(): LiveData<MoviesState> = mediatorStateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null
    private val movieSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        searchRequest(changedText)
    }

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
        if (latestSearchText != changedText) {
            this.latestSearchText = changedText
            movieSearchDebounce(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(MoviesState.Loading)

            viewModelScope.launch {
                moviesInteractor.searchMovies(newSearchText).collect { pair->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundMovies: List<Movie>?, message: String?) {
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

    private fun renderState(state: MoviesState) {
        stateLiveData.postValue(state)
    }

}