package com.example.moviescleanarchitectureex.domen.impl

import com.example.moviescleanarchitectureex.domen.api.NamesInteractor
import com.example.moviescleanarchitectureex.domen.api.NamesRepository
import com.example.moviescleanarchitectureex.util.Resource
import java.util.concurrent.Executors
import javax.inject.Inject

class NamesInteracrorImpl @Inject constructor(private val repository: NamesRepository) : NamesInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchNames(expression: String, consumer: NamesInteractor.NamesConsumer) {
        executor.execute {
            when(val resource = repository.searchNames(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(resource.data, resource.message) }
            }
        }
    }
}