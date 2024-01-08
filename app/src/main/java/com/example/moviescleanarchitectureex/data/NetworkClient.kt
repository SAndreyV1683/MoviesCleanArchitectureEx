package com.example.moviescleanarchitectureex.data

import com.example.moviescleanarchitectureex.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}