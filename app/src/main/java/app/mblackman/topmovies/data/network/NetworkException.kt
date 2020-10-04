package app.mblackman.topmovies.data.network

/**
 * An exception that occurs from a network adapter.
 */
class NetworkException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
