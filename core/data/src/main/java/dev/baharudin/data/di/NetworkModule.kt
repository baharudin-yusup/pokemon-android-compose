package dev.baharudin.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.baharudin.common.util.Constants
import dev.baharudin.common.util.Logger
import dev.baharudin.data.local.database.PokemonDatabase
import dev.baharudin.data.local.database.dao.PokemonBackpackDao
import dev.baharudin.data.local.database.dao.PokemonDao
import dev.baharudin.data.local.database.dao.PokemonUserDataDao
import dev.baharudin.data.local.entity.RemoteKeysDao
import dev.baharudin.data.network.api.PokeApiService
import dev.baharudin.data.network.interceptors.LoggingInterceptor
import dev.baharudin.data.paging.PokemonRemoteMediator
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor.create())
            .connectTimeout(dev.baharudin.data.BuildConfig.API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(dev.baharudin.data.BuildConfig.API_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(dev.baharudin.data.BuildConfig.API_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(dev.baharudin.data.BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun providePokeApiService(retrofit: Retrofit): PokeApiService {
        return retrofit.create(PokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            Constants.Database.NAME
        )
            .addMigrations(

            )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.pokemonDao()
    }

    @Provides
    fun provideRemoteKeysDao(database: PokemonDatabase): RemoteKeysDao {
        return database.remoteKeysDao()
    }

    @Provides
    fun providePokemonUserDataDao(database: PokemonDatabase): PokemonUserDataDao {
        return database.pokemonUserDataDao()
    }

    @Provides
    fun providePokemonBackpackDao(database: PokemonDatabase): PokemonBackpackDao {
        return database.pokemonBackpackDao()
    }

    @Provides
    @Singleton
    fun providePokemonRemoteMediator(
        database: PokemonDatabase,
        apiService: PokeApiService,
        logger: Logger
    ): PokemonRemoteMediator {
        return PokemonRemoteMediator(database, apiService, logger)
    }
}

