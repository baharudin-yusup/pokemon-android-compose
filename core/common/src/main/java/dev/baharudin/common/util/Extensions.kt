package dev.baharudin.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Extension function to convert a Flow to a Result Flow.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .catch { emit(Result.Error(it)) }
}

/**
 * Extension function to safely execute a suspend function and return Result.
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Exception) {
        Result.Error(e)
    }
}

/**
 * Extension function to get error message from Result.
 */
fun Result<*>.errorMessage(): String? {
    return when (this) {
        is Result.Error -> exception.message ?: "Unknown error"
        else -> null
    }
}


