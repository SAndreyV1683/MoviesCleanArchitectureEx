package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.NamesInteractor
import com.example.moviescleanarchitectureex.domen.api.NamesRepository
import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors
import javax.inject.Inject

class NamesInteracrorImpl @Inject constructor(private val repository: NamesRepository) : NamesInteractor {

    override suspend fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>> {
        return repository.searchNames(expression).map { result->
            when(result) {
                is Resource.Success -> { Pair(result.data, null)}
                is Resource.Error -> { Pair(null, result.message) }
            }
        }
    }
}