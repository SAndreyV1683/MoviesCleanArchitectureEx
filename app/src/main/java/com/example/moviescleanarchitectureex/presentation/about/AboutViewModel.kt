package com.example.moviescleanarchitectureex.presentation.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.models.MovieDetails
import com.example.moviescleanarchitectureex.ui.models.AboutState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AboutViewModel(
    private val movieId: String,
    private val moviesInteractor: MoviesInteractor
): ViewModel() {

    private val stateLiveData = MutableLiveData<AboutState>()
    fun observeState(): LiveData<AboutState> = stateLiveData

    init {
        viewModelScope.launch {
            moviesInteractor.getMovieDetails(movieId).collect { pair->
                processResult(pair.first, pair.second)
            }
        }
    }

    private fun processResult(movieDetails: MovieDetails?, errorMessage: String?) {
        if (movieDetails != null) {
            stateLiveData.postValue(AboutState.Content(movieDetails))
        } else {
            stateLiveData.postValue(AboutState.Error(errorMessage ?: "Unknown error"))
        }
    }


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