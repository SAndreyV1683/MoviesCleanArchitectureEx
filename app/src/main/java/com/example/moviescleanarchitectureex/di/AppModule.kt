package com.example.moviescleanarchitectureex.di

import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.data.NetworkClient
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.data.network.IMDbApiService
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.impl.MoviesInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(
    includes = [BindModule::class, NetworkModule::class]
)
class AppModule {
    @Provides
    fun provideMoviesRepository(
        networkClient: NetworkClient,
        localStorage: LocalStorage
    ): MoviesRepository {
        return MoviesRepositoryImpl(
            networkClient,
            localStorage
        )
    }

}

@Module
interface BindModule {

    /**To provide implementation rather than implementation of MoviesInteractorImpl and NetworkClient**/
    @Binds
    fun bindsMoviesInteractor(moviesInteractorImpl: MoviesInteractorImpl): MoviesInteractor
    @Binds
    fun bindsNetworkClient(retrofitNetworkClient: RetrofitNetworkClient): NetworkClient

}



@Module
class NetworkModule {
    @Provides
    fun provideImdbApiService(): IMDbApiService {
        val imdbBaseUrl = "https://tv-api.com"
        val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()
        val retrofit =  Retrofit.Builder()
            .baseUrl(imdbBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(IMDbApiService::class.java)
    }
}
