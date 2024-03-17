package com.example.moviescleanarchitectureex.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.data.NetworkClient
import com.example.moviescleanarchitectureex.data.dto.MovieCastRequest
import com.example.moviescleanarchitectureex.data.dto.MovieDetailsRequest
import com.example.moviescleanarchitectureex.data.dto.MoviesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.NamesSearchRequest
import com.example.moviescleanarchitectureex.data.dto.Response
import javax.inject.Inject

class  RetrofitNetworkClient @Inject constructor(
    private val imDbApiService: IMDbApiService
): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1}
        }

        if ((dto !is MoviesSearchRequest)
            && (dto !is MovieDetailsRequest)
            && (dto !is MovieCastRequest)
            && (dto !is NamesSearchRequest)
        ) {
            return Response().apply { resultCode = 400 }
        }

        val response = when (dto) {
            is NamesSearchRequest -> {
                imDbApiService.searchNames(dto.expression).execute()
            }

            is MoviesSearchRequest -> {
                imDbApiService.findMovies(dto.expression).execute()
            }

            is MovieDetailsRequest -> {
                imDbApiService.getMovieDetails(dto.id).execute()
            }

            else -> {
                imDbApiService.getFullCast((dto as MovieCastRequest).movieId).execute()
            }
        }

        val body = response.body()
        return body?.apply { resultCode = response.code() } ?: Response().apply { resultCode = response.code() }

    }

    private fun isConnected(): Boolean {
        val connectivityManager = MoviesApplication.INSTANCE.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}