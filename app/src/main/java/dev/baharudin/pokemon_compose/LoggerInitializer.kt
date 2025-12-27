package dev.baharudin.pokemon_compose

import dev.baharudin.common.util.Logger
import timber.log.Timber

/**
 * Timber-based logger implementation.
 *
 * This class implements the Logger interface from core:common using Timber.
 */
class TimberLogger : Logger {
    override fun d(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).d(message, *args)
    }

    override fun d(tag: String, throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.tag(tag).d(throwable, message, *args)
    }

    override fun i(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).i(message, *args)
    }

    override fun i(tag: String, throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.tag(tag).i(throwable, message, *args)
    }

    override fun w(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).w(message, *args)
    }

    override fun w(tag: String, throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.tag(tag).w(throwable, message, *args)
    }

    override fun e(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).e(message, *args)
    }

    override fun e(tag: String, throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.tag(tag).e(throwable, message, *args)
    }

    override fun v(tag: String, message: String, vararg args: Any?) {
        Timber.tag(tag).v(message, *args)
    }

    override fun v(tag: String, throwable: Throwable?, message: String, vararg args: Any?) {
        Timber.tag(tag).v(throwable, message, *args)
    }
}

/**
 * Initialize Timber for logging.
 *
 * This function initializes Timber with DebugTree. Logger instances are provided via Hilt DI using
 * LoggingModule.
 *
 * @param debugMode Whether to use debug logging. Should be set based on BuildConfig.DEBUG.
 */
fun initLogger(debugMode: Boolean = true) {
    if (debugMode) {
        Timber.plant(Timber.DebugTree())
    } else {
        // TODO: Implement release logging
    }
}
