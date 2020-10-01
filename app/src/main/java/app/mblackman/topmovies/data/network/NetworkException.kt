package app.mblackman.topmovies.data.network

/**
 * An exception that occurs from a network adapter.
 *
 * @param message A message to detail the exception.
 */
class NetworkException(message: String) : Exception(message) {
}