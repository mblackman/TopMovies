package app.mblackman.topmovies.data.network.localhost

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

/**
 * The Movie service API.
 */
interface ApiService {
    /**
     * Gets the top movies.
     */
    @GET("/api/movies")
    fun getMoviesAsync(): Deferred<Response<List<Movie>>>
}