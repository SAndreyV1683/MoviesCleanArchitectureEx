package com.example.moviescleanarchitectureex.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class PosterViewModel(
    posterUrl: String
): ViewModel() {
    private val urlLiveData = MutableLiveData(posterUrl)
    fun observeUrl(): LiveData<String> = urlLiveData

    class PosterViewModelFactory @AssistedInject constructor(
        @Assisted("newPosterUrl") private val posterUrl: String
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PosterViewModel(posterUrl) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted("newPosterUrl") posterUrl: String): PosterViewModelFactory
        }
    }

}