package app.mblackman.topmovies.data.network

/**
 * An exception that occurs from a network adapter.
 */
class NetworkException(message: String, cause: Throwable) : Exception(message, cause) {
}
