package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Person
import kotlinx.coroutines.flow.Flow

interface NamesInteractor {

    suspend fun searchNames(expression: String): Flow<Pair<List<Person>?, String?>>


}