package com.example.moviescleanarchitectureex.di

import android.content.Context
import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.data.NetworkClient
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.impl.MoviesInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [AppModule.NetworkModule::class, AppModule.LocalStorageModule::class, AppModule.AppBindModule::class, AppModule.MoviesInteractorModule::class])
class AppModule {

    @Provides
    fun provideMoviesRepository(
        networkClient: NetworkClient,
        localStorage: LocalStorage
    ): MoviesRepository{
        return MoviesRepositoryImpl(
            networkClient,
            localStorage
        )
    }
    @Provides
    fun provideMoviesRepositoryImpl(
        networkClient: NetworkClient,
        localStorage: LocalStorage
    ): MoviesRepositoryImpl {
        return MoviesRepositoryImpl(
            networkClient,
            localStorage
        )
    }

    @Module
    class MoviesInteractorModule {
        @Provides
        fun provideMoviesInteractor(
            moviesRepository: MoviesRepository
        ): MoviesInteractorImpl {
            return MoviesInteractorImpl(moviesRepository)
        }
    }

    @Module
    interface AppBindModule {
        @Binds
        fun bindMoviesInteractorImplToMoviesInteractor(
            moviesInteractorImpl: MoviesInteractorImpl
        ): MoviesInteractor
    }

    @Module
    class NetworkModule {

        @Provides
        fun provideNetworkClient(
            retrofitNetworkClient: RetrofitNetworkClient
        ): NetworkClient {
            return retrofitNetworkClient
        }
        @Provides
        fun provideRetrofitNetworkClient(
            context: Context
        ): RetrofitNetworkClient {
            return RetrofitNetworkClient(context)
        }
    }

    @Module
    class LocalStorageModule{
        @Provides
        fun provideLocalStorage(
            context: Context
        ): LocalStorage {
            return LocalStorage(context)
        }
    }
}