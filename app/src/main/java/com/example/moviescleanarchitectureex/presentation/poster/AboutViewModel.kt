package com.example.moviescleanarchitectureex.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.ui.models.AboutState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<AboutState>()
    fun observeState(): LiveData<AboutState> = stateLiveData

    init {
        moviesInteractor.getMovieDetails(
            movieId,
            object : MoviesInteractor.MovieDetailsConsumer {
                override fun consume(movieDetails: MovieDetails?, errorMessage: String?) {
                    if (movieDetails != null) {
                        stateLiveData.postValue(AboutState.Content(movieDetails))
                    } else {
                        stateLiveData.postValue(AboutState.Error(errorMessage ?: "Unknown error"))
                    }
                }
            }
        )
    }

    @Suppress("UNCHECKED_CAST")
    class AboutViewModelFactory @AssistedInject constructor(
        @Assisted("newMovieId") private val movieId: String,
        private val moviesInteractor: MoviesInteractor
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AboutViewModel(movieId, moviesInteractor) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted("newMovieId") movieId: String): AboutViewModelFactory
        }
    }

}