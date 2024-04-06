package com.example.moviescleanarchitectureex.presentation.names


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviescleanarchitectureex.R
import com.example.moviescleanarchitectureex.domen.api.NamesInteractor
import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.presentation.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class NamesViewModel @Inject constructor (
    private val application: Application,
    private val namesInteractor: NamesInteractor
) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(
           namesInteractor: NamesInteractor
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                NamesViewModel(
                    this[APPLICATION_KEY] as Application,
                    namesInteractor,
                )
            }
        }
    }

    private val stateLiveData = MutableLiveData<NamesState>()
    fun observeState(): LiveData<NamesState> = stateLiveData

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    private var latestSearchText: String? = null

    private var searchJob: Job? = null


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(NamesState.Loading)

            viewModelScope.launch {
                namesInteractor.searchNames(newSearchText).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundNames: List<Person>?, errorMessage: String?) {
        val persons = mutableListOf<Person>()
        if (foundNames != null) {
            persons.addAll(foundNames)
        }

        when {
            errorMessage != null -> {
                renderState(
                    NamesState.Error(
                        message = application.getString(
                            R.string.something_went_wrong),
                    )
                )
                showToast.postValue(errorMessage)
            }

            persons.isEmpty() -> {
                renderState(
                    NamesState.Empty(
                        message = application.getString(R.string.nothing_found),
                    )
                )
            }

            else -> {
                renderState(
                    NamesState.Content(
                        persons = persons,
                    )
                )
            }
        }
    }

    private fun renderState(state: NamesState) {
        stateLiveData.postValue(state)
    }
}