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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class  RetrofitNetworkClient @Inject constructor(
    private val imDbApiService: IMDbApiService
): NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
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

        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is NamesSearchRequest -> {
                        val response = imDbApiService.searchNames(dto.expression)
                        response.apply { resultCode = 200 }
                    }

                    is MoviesSearchRequest -> {
                        val response = imDbApiService.findMovies(dto.expression)
                        response.apply { resultCode = 200 }
                    }

                    is MovieDetailsRequest -> {
                        val response = imDbApiService.getMovieDetails(dto.id)
                        response.apply { resultCode = 200 }
                    }

                    else -> {
                        val response = imDbApiService.getFullCast((dto as MovieCastRequest).movieId)
                        response.apply { resultCode = 200 }
                    }
                }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
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