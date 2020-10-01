package app.mblackman.topmovies.data.database

import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import kotlinx.coroutines.flow.Flow

/**
 * A data store for persisting and retrieving [Movie]s.
 */
interface MovieStore {
    /**
     * Inserts all the movies in the store. This will not replace [Movie]s with existing ids.
     */
    fun insertMovies(movies: List<Movie>)

    /**
     * Updates a [Movie] in the storage.
     */
    fun updateMovie(movie: Movie)

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    fun getMovies(filters: MovieFilter? = null) : Flow<List<Movie>>
}