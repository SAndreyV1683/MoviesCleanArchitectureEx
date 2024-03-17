package com.example.moviescleanarchitectureex.domen.api

import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.util.Resource

interface NamesRepository {
    fun searchNames(expression: String): Resource<List<Person>>

}