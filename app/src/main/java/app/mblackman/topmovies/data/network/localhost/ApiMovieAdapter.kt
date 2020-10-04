package app.mblackman.topmovies.data.network.localhost

import app.mblackman.topmovies.data.common.Failure
import app.mblackman.topmovies.data.common.Result
import app.mblackman.topmovies.data.common.Success
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.*
import retrofit2.Response
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
    override suspend fun getTopMovies(): Result<List<Movie>> =
        safeApiRequest(
            mappingFunc = { movies -> movies.mapIndexed { i, movie -> movie.toDomainObject(i.toLong()) } },
            request = { apiService.getMoviesAsync().await() }
        )

    private suspend fun <T, V> safeApiRequest(
        mappingFunc: (T) -> V, request: suspend () -> Response<T>
    ): Result<V> {
        val response = try {
            request()
        } catch (e: Throwable) {
            return Failure(e)
        }

        if (response.isSuccessful) {
            val body =
                response.body() ?: return Failure(Exception("Response body was empty."))

            return Success(mappingFunc(body))
        } else {
            return Failure(Exception(response.message()))
        }
    }
}