package com.example.moviescleanarchitectureex.presentation.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.db.HistoryInteractor
import com.example.moviescleanarchitectureex.domen.models.Movie
import kotlinx.coroutines.launch


class HistoryViewModel(
    private val application: Application,
    private val historyInteractor: HistoryInteractor
): AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<HistoryState>()

    fun observeState(): LiveData<HistoryState> = stateLiveData

    fun fillData() {
        renderState(HistoryState.Loading)
        viewModelScope.launch {
            historyInteractor
                .historyMovies()
                .collect { movies ->
                    processResult(movies)
                }
        }
    }

    private fun processResult(movies: List<Movie>) {
        if (movies.isEmpty()) {
            renderState(HistoryState.Empty(application.getString(R.string.nothing_found)))
        } else {
            renderState(HistoryState.Content(movies))
        }
    }

    private fun renderState(state: HistoryState) {
        stateLiveData.postValue(state)
    }

    companion object {
        fun getViewModelFactory(
            historyInteractor: HistoryInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                HistoryViewModel(
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application,
                    historyInteractor
                )
            }
        }
    }

}