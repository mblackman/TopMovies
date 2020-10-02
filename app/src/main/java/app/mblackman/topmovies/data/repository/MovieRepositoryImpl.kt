package app.mblackman.topmovies.data.repository

import app.mblackman.topmovies.dagger.DefaultDispatcher
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.database.*
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.MovieAdapter
import app.mblackman.topmovies.data.network.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
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
                movieDatabase.movieDao.insertAll(movies.result.map { it.toDatabaseObject() })

                movies.result.forEach { movie ->
                    movieDatabase.ratingDao.insertAll(movie.ratings.map { it.toDatabaseObject(movie.id) })
                    movieDatabase.genreDao.insertAll(movie.genres.map { Genre(it, movie.id) })
                    movieDatabase.writerDao.insertAll(movie.writers.map { Writer(it, movie.id) })
                    movieDatabase.actorDao.insertAll(movie.actors.map { Actor(it, movie.id) })
                    movieDatabase.languageDao.insertAll(movie.languages.map {
                        Language(
                            it,
                            movie.id
                        )
                    })
                    movieDatabase.countryDao.insertAll(movie.countries.map {
                        Country(
                            it,
                            movie.id
                        )
                    })
                }
                true
            }
            else -> false
        }
    }

    /**
     * Updates the movie in the store.
     */
    override fun updateMovie(movie: Movie) {
        movieDatabase.movieDao.updateMovie(movie.toDatabaseObject())
    }

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    override fun getMovies(filter: MovieFilter?) =
        filter.getMoviesFlow()
            .map { movies -> movies.map { it.toDomainObject() } }
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
                require(this.isFavorite != null) { "Only one filter can be applied with the Room data source." }
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