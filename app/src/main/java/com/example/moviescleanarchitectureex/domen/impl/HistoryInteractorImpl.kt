package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.db.HistoryInteractor
import com.example.moviescleanarchitectureex.domen.db.HistoryRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryInteractorImpl @Inject constructor(
    private val historyRepository: HistoryRepository
): HistoryInteractor {
    override fun historyMovies(): Flow<List<Movie>> {
        return historyRepository.historyMovies()
    }

}