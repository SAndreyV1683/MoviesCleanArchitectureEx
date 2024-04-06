package com.example.moviescleanarchitectureex.presentation.moviescast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class MoviesCastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): LiveData<MoviesCastState> = stateLiveData

    init {
        stateLiveData.postValue(MoviesCastState.Loading)

        viewModelScope.launch {
            moviesInteractor.getMoviesCast(movieId).collect { pair ->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(movieCast: MovieCast?, errorMessage: String?) {
        if (movieCast != null) {
            stateLiveData.postValue(castToUiStateContent(movieCast))
        } else {
            stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
        }
    }

    private fun castToUiStateContent(cast: MovieCast): MoviesCastState {
        val items = buildList<MoviesCastRVItem> {
            if (cast.directors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Directors")
                this += cast.directors.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.writers.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Writers")
                this += cast.writers.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.actors.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Actors")
                this += cast.actors.map { MoviesCastRVItem.PersonItem(it) }
            }
            if (cast.others.isNotEmpty()) {
                this += MoviesCastRVItem.HeaderItem("Others")
                this += cast.others.map { MoviesCastRVItem.PersonItem(it) }
            }
        }

        return MoviesCastState.Content(
            fullTitle = cast.fullTitle,
            items = items
        )
    }

    class MovieCastViewModelFactory @AssistedInject constructor(
        @Assisted("newMovieId") private val movieId: String,
        private val moviesInteractor: MoviesInteractor
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MoviesCastViewModel(movieId, moviesInteractor) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted("newMovieId") movieId: String): MovieCastViewModelFactory
        }
    }

}