package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.dagger.DefaultDispatcher
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.database.*
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.MovieAdapter
import app.mblackman.topmovies.data.network.Success
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Manages [Movie] data and its persistence.
 */
class MovieRepositoryImpl @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val adapter: MovieAdapter,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) :
    MovieRepository {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Gets movies from the adapter and updates the storage.
     *
     * @return True if the update was a success, else false.
     */
    override suspend fun getUpdatedMovies(): Boolean {
        return when (val movies = adapter.getTopMovies()) {
            is Success -> {
                withContext(Dispatchers.IO) {
                    movieDatabase.movieDao.insertMovieWithDetails(movies.result.map(Movie::toDatabaseObject))
                }
                true
            }
            else -> false
        }
    }

    /**
     * Updates the movie in the store.
     */
    override suspend fun updateMovie(movie: Movie) {
        withContext(defaultDispatcher) {
            movieDatabase.movieDao.updateMovie(movie.toDatabaseObject().movie)
        }
    }

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    override fun getMovies(filter: MovieFilter?) =
        filter.getMoviesFlow()
            .map { movies -> movies.map { it.toDomainObject() } }
            .flowOn(defaultDispatcher)
            .conflate()

    /**
     * Gets the movie with the given id.
     */
    override fun getMovie(id: Long): Flow<Movie?> =
        movieDatabase.movieDao.getMovie(id)
            .map { movie -> movie?.toDomainObject() }
            .flowOn(defaultDispatcher)
            .conflate()

    private fun MovieFilter?.getMoviesFlow() =
        when {
            this == null -> {
                movieDatabase.movieDao.getMovies()
            }
            this.genres != null -> {
                require(this.year == null && this.isFavorite == null) { "Only one filter can be applied with the Room data source." }
                movieDatabase.movieDao.getMoviesByGenre(this.genres, this.genres.size)
            }
            this.year != null -> {
                require(this.isFavorite == null) { "Only one filter can be applied with the Room data source." }
                movieDatabase.movieDao.getMoviesByYear(this.year)
            }
            this.isFavorite != null -> {
                movieDatabase.movieDao.getMoviesByFavorite(this.isFavorite)
            }
            else -> {
                movieDatabase.movieDao.getMovies()
            }
        }
}