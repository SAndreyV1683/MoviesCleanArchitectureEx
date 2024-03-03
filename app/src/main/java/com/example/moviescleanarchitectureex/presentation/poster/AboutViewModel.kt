package com.example.moviescleanarchitectureex.presentation.poster

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.ui.models.AboutState

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

    companion object {
        fun getViewModelFactory(movieId: String, moviesInteractor: MoviesInteractor): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")

                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return AboutViewModel(
                        movieId,
                        // 3
                        moviesInteractor,
                    ) as T
                }
            }
    }
}