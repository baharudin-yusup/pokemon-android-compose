package dev.baharudin.common.util

/**
 * A sealed class representing the result of an operation that can either succeed or fail.
 *
 * @param T The type of data returned on success
 */
sealed class Result<out T> {
    /**
     * Represents a successful operation with data.
     */
    data class Success<T>(val data: T) : Result<T>()

    /**
     * Represents a failed operation with an error.
     */
    data class Error(val exception: Throwable) : Result<Nothing>()

    /**
     * Represents a loading state.
     */
    object Loading : Result<Nothing>()

    /**
     * Returns true if the result is a success.
     */
    val isSuccess: Boolean
        get() = this is Success

    /**
     * Returns true if the result is an error.
     */
    val isError: Boolean
        get() = this is Error

    /**
     * Returns true if the result is loading.
     */
    val isLoading: Boolean
        get() = this is Loading

    /**
     * Returns the data if successful, null otherwise.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    /**
     * Returns the exception if error, null otherwise.
     */
    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }
}

/**
 * Extension function to map the success value.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
    is Result.Loading -> this
}

/**
 * Extension function to handle success and error cases.
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

/**
 * Extension function to handle error cases.
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}

/**
 * Extension function to handle loading cases.
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

