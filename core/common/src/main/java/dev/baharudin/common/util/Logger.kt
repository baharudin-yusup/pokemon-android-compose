package dev.baharudin.common.util

/**
 * Logging interface.
 */
interface Logger {
    /**
     * Debug log.
     */
    fun d(tag: String, message: String, vararg args: Any?)
    /**
     * Debug log with throwable.
     */
    fun d(tag: String, throwable: Throwable?, message: String, vararg args: Any?)
    /**
     * Info log.
     */
    fun i(tag: String, message: String, vararg args: Any?)
    /**
     * Info log with throwable.
     */
    fun i(tag: String, throwable: Throwable?, message: String, vararg args: Any?)
    /**
     * Warning log.
     */
    fun w(tag: String, message: String, vararg args: Any?)
    /**
     * Warning log with throwable.
     */
    fun w(tag: String, throwable: Throwable?, message: String, vararg args: Any?)
    /**
     * Error log.
     */
    fun e(tag: String, message: String, vararg args: Any?)
    /**
     * Error log with throwable.
     */
    fun e(tag: String, throwable: Throwable?, message: String, vararg args: Any?)
    /**
     * Verbose log.
     */
    fun v(tag: String, message: String, vararg args: Any?)
    /**
     * Verbose log with throwable.
     */
    fun v(tag: String, throwable: Throwable?, message: String, vararg args: Any?)
}
