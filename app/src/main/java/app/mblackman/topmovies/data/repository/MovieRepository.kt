package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.common.Result

import kotlinx.coroutines.flow.Flow

/**
 * Represents the interface for storing and retrieving [Movie]s.
 */
interface MovieRepository {
    /**
     * Gets movies from the adapter and updates the storage.
     *
     * @return True if the update was a success, else false.
     */
    suspend fun getUpdatedMovies(): Result<Unit>

    /**
     * Updates the movie in the store.
     */
    suspend fun updateMovie(movie: Movie)

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    fun getMovies(filter: MovieFilter? = null): Flow<List<Movie>>

    /**
     * Gets the movie with the given id.
     */
    fun getMovie(id: Long): Flow<Movie?>
}