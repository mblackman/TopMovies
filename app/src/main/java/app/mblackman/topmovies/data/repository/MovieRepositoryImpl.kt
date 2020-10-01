package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.database.MovieStore
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.MovieAdapter
import app.mblackman.topmovies.data.network.Success
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Manages [Movie] data and its persistence.
 */
class MovieRepositoryImpl @Inject constructor(
    private val store: MovieStore,
    private val adapter: MovieAdapter
) :
    MovieRepository {
    /**
     * Gets movies from the adapter and updates the storage.
     *
     * @return True if the update was a success, else false.
     */
    override suspend fun getUpdatedMovies(): Boolean {
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
    override fun updateMovie(movie: Movie) {
        store.updateMovie(movie)
    }

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    override fun getMovies(filter: MovieFilter?) = store.getMovies(filter)
}