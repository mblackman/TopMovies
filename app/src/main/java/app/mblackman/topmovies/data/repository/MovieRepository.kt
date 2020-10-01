package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    /**
     * Gets movies from the adapter and updates the storage.
     *
     * @return True if the update was a success, else false.
     */
    suspend fun getUpdatedMovies(): Boolean

    /**
     * Updates the movie in the store.
     */
    fun updateMovie(movie: Movie)

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    fun getMovies(filter: MovieFilter? = null): Flow<List<Movie>>
}