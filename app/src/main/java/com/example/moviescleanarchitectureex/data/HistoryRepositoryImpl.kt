package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.converters.MovieDbConvertor
import com.example.moviescleanarchitectureex.data.db.AppDatabase
import com.example.moviescleanarchitectureex.data.db.MovieEntity
import com.example.moviescleanarchitectureex.domen.db.HistoryRepository
import com.example.moviescleanarchitectureex.domen.models.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val movieDbConvertor: MovieDbConvertor
): HistoryRepository {
    override fun historyMovies(): Flow<List<Movie>> = flow {
        val movies = appDatabase.movieDao().getMovies()
        emit(convertFromMovieEntity(movies))
    }

    private fun convertFromMovieEntity(movies: List<MovieEntity>) : List<Movie> {
        return movies.map { movie -> movieDbConvertor.map(movie) }
    }

}