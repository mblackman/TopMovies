package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.data.database.MovieStore
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.MovieAdapter
import app.mblackman.topmovies.data.network.Success

/**
 * Manages [Movie] data and its persistence.
 */
class MovieRepository(private val store: MovieStore, private val adapter: MovieAdapter) {
    /**
     * Gets movies from the adapter and updates the storage.
     *
     * @return True if the update was a success, else false.
     */
    suspend fun getUpdatedMovies(): Boolean {
        return when (val movies = adapter.getTopMovies()) {
            is Success -> {
                store.insertMovies(movies.result)
                true
            }
            else -> false
        }
    }

    /**
     * Updates the movie in the store.
     */
    fun updateMovie(movie: Movie) {
        store.updateMovie(movie)
    }
}