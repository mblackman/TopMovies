package app.mblackman.topmovies.data.network.localhost

import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.*
import javax.inject.Inject

/**
 * A [MovieAdapter] that consumes [ApiService] for movie data.
 *
 * @param apiService The service to get the movie data from.
 */
class ApiMovieAdapter @Inject constructor(private val apiService: ApiService) : MovieAdapter {
    /**
     * Gets the list of top [Movie]s.
     */
    override suspend fun getTopMovies(): Result<List<Movie>> {
        val response = apiService.getMoviesAsync().await()

        if (response.isSuccessful) {
            val body = response.body() ?: return Failure(NetworkException("Movie body was empty."))

            return Success(body.mapIndexed { i, movie -> movie.toDomainObject(i.toLong()) })
        } else {
            return Failure(NetworkException(response.message()))
        }
    }
}