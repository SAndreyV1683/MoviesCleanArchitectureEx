package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.dto.NamesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.NamesSearchResponse
import com.example.moviescleanarchitectureex.domen.api.NamesRepository
import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.util.Resource
import javax.inject.Inject

class NamesRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) : NamesRepository {

    override fun searchNames(expression: String): Resource<List<Person>> {
        val response = networkClient.doRequest(NamesSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                with(response as NamesSearchResponse) {
                    Resource.Success(results.map {
                        Person(id = it.id,
                            name = it.title,
                            description = it.description,
                            photoUrl = it.image)
                    })
                }
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}