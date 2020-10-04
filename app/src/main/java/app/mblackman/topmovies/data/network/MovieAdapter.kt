package app.mblackman.topmovies.data.network

import app.mblackman.topmovies.data.common.Result
import app.mblackman.topmovies.data.domain.Movie

/**
 * Handles getting movie data from a data source.
 */
interface MovieAdapter {
    /**
     * Gets the list of top [Movie]s.
     */
    suspend fun getTopMovies(): Result<List<Movie>>
}