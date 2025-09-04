/**
 * Interface for logging with platform specific implementations
 */
interface Logger {

    /**
     * Method for debug scope logging
     */
    fun d(tag: String, message: String)

    /**
     * Method for error scope logging
     */
    fun e(tag: String, message: String)

    /**
     * Method for informative scope logging
     */
    fun i(tag: String, message: String)
}
