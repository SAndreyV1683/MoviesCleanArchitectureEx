package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.dto.NamesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.NamesSearchResponse
import com.example.moviescleanarchitectureex.domen.api.NamesRepository
import com.example.moviescleanarchitectureex.domen.models.Person
import com.example.moviescleanarchitectureex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NamesRepositoryImpl @Inject constructor(private val networkClient: NetworkClient) : NamesRepository {

    override suspend fun searchNames(expression: String): Flow<Resource<List<Person>>> = flow {
        val response = networkClient.doRequest(NamesSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as NamesSearchResponse) {
                    val data = Resource.Success(results.map {
                        Person(id = it.id,
                            name = it.title,
                            description = it.description,
                            photoUrl = it.image)
                    })
                    emit(data)
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}