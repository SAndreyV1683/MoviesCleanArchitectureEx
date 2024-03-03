package com.example.moviescleanarchitectureex.presentation.poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class PosterViewModel(
    private val posterUrl: String
): ViewModel() {
    private val urlLiveData = MutableLiveData(posterUrl)
    fun observeUrl(): LiveData<String> = urlLiveData

    companion object {
        fun getViewModelFactory(posterUrl: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PosterViewModel(posterUrl)
            }
        }
    }

}