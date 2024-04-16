package com.example.moviescleanarchitectureex.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.moviescleanarchitectureex.MoviesApplication
import com.example.moviescleanarchitectureex.data.HistoryRepositoryImpl

import com.example.moviescleanarchitectureex.data.MoviesRepositoryImpl
import com.example.moviescleanarchitectureex.data.NamesRepositoryImpl
import com.example.moviescleanarchitectureex.data.NetworkClient
import com.example.moviescleanarchitectureex.data.converters.MovieDbConvertor
import com.example.moviescleanarchitectureex.data.db.AppDatabase
import com.example.moviescleanarchitectureex.data.localstorage.LocalStorage
import com.example.moviescleanarchitectureex.data.network.IMDbApiService
import com.example.moviescleanarchitectureex.data.network.RetrofitNetworkClient
import com.example.moviescleanarchitectureex.domen.api.MoviesInteractor
import com.example.moviescleanarchitectureex.domen.api.MoviesRepository
import com.example.moviescleanarchitectureex.domen.api.NamesInteractor
import com.example.moviescleanarchitectureex.domen.api.NamesRepository
import com.example.moviescleanarchitectureex.domen.db.HistoryInteractor
import com.example.moviescleanarchitectureex.domen.db.HistoryRepository
import com.example.moviescleanarchitectureex.domen.impl.HistoryInteractorImpl
import com.example.moviescleanarchitectureex.domen.impl.MoviesInteractorImpl
import com.example.moviescleanarchitectureex.domen.impl.NamesInteracrorImpl
import com.example.moviescleanarchitectureex.presentation.names.NamesViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module(
    includes = [BindModule::class, NetworkModule::class, DataBaseModule::class]
)
class AppModule {
    @Provides
    fun provideMoviesRepository(
        networkClient: NetworkClient,
        localStorage: LocalStorage,
        appDatabase: AppDatabase,
        movieDbConvertor: MovieDbConvertor
    ): MoviesRepository {
        return MoviesRepositoryImpl(
            networkClient,
            localStorage,
            appDatabase,
            movieDbConvertor
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
    @Binds
    fun bindsNamesInteractor(namesInteractorImpl: NamesInteracrorImpl): NamesInteractor
    @Binds
    fun bindsNamesRepository(namesRepositoryImpl: NamesRepositoryImpl): NamesRepository
    @Binds
    fun bindsHistoryRepository(historyRepositoryImpl: HistoryRepositoryImpl): HistoryRepository
    @Binds
    fun bindsHistoryInteractor(historyInteractorImpl: HistoryInteractorImpl): HistoryInteractor

    @Binds
    @IntoMap
    @ViewModelKey(NamesViewModel::class)
    fun bindMyViewModel(view: NamesViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

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

@Module
class DataBaseModule {
    @Provides
    fun provideAppDataBase(): AppDatabase {
        return Room.databaseBuilder(context = MoviesApplication.INSTANCE, AppDatabase::class.java, "database.db").build()
    }
}


@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull()

            ?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        return creator.get() as T
    }
}

