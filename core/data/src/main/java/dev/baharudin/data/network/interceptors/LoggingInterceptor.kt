package dev.baharudin.data.network.interceptors

import dev.baharudin.data.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

internal object LoggingInterceptor {
    fun create(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = if (BuildConfig.ENABLE_API_LOGGING) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}

