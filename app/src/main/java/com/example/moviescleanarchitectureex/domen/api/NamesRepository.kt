package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow

interface NamesRepository {
    suspend fun searchNames(expression: String): Flow<Resource<List<Person>>>

}