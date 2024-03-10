package com.example.moviescleanarchitectureex.presentation.moviescast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.MovieCast
import com.example.moviescleanarchitectureex.presentation.about.AboutViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MoviesCastViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor
): ViewModel() {
    private val stateLiveData = MutableLiveData<MoviesCastState>()
    fun observeState(): LiveData<MoviesCastState> = stateLiveData

    init {
        stateLiveData.postValue(MoviesCastState.Loading)

        moviesInteractor.getMoviesCast(movieId, object: MoviesInteractor.MovieCastConsumer {
            override fun consume(movieCast: MovieCast?, errorMessage: String?) {
                if (movieCast != null) {
                    stateLiveData.postValue(MoviesCastState.Content(movieCast))
                } else {
                    stateLiveData.postValue(MoviesCastState.Error(errorMessage ?: "Unknown error"))
                }
            }
        })
    }

    /*class MovieCastViewModelFactory @AssistedInject constructor(
        @Assisted("newMovieId") private val movieId: String,
        private val moviesInteractor: MoviesInteractor
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MoviesCastViewModel(movieId, moviesInteractor) as T
        }
        @AssistedFactory
        interface Factory {
            fun create(@Assisted("newMovieId") movieId: String): MovieCastViewModelFactory
        }
    }*/

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