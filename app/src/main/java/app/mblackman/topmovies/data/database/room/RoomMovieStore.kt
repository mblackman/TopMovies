package app.mblackman.topmovies.data.database.room

import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.database.MovieStore
import app.mblackman.topmovies.data.domain.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomMovieStore @Inject constructor(private val database: MovieDatabase) : MovieStore {
    /**
     * Inserts all the movies in the store. This will not replace [Movie]s with existing ids.
     */
    override fun insertMovies(movies: List<Movie>) {
        database.movieDao.insertAll(movies.map { it.toDatabaseObject() })

        movies.forEach { movie ->
            database.ratingDao.insertAll(movie.ratings.map { it.toDatabaseObject(movie.id) })
            database.genreDao.insertAll(movie.genres.map { Genre(it, movie.id) })
            database.writerDao.insertAll(movie.writers.map { Writer(it, movie.id) })
            database.actorDao.insertAll(movie.actors.map { Actor(it, movie.id) })
            database.languageDao.insertAll(movie.languages.map {
                Language(
                    it,
                    movie.id
                )
            })
            database.countryDao.insertAll(movie.countries.map {
                Country(
                    it,
                    movie.id
                )
            })
        }
    }

    /**
     * Updates a [Movie] in the storage.
     */
    override fun updateMovie(movie: Movie) {
        database.movieDao.updateMovie(movie.toDatabaseObject())
    }

    /**
     * Gets a [Flow] with the list of [Movie]s for the given filter.
     */
    override fun getMovies(filters: MovieFilter?): Flow<List<Movie>> = flow {
        val movieFlow = when {
            filters == null -> {
                database.movieDao.getMovies()
            }
            filters.genres != null -> {
                require(filters.year == null && filters.isFavorite == null) { "Only one filter can be applied with the Room data source." }
                database.movieDao.getMoviesByGenre(filters.genres, filters.genres.size)
            }
            filters.year != null -> {
                require(filters.isFavorite != null) { "Only one filter can be applied with the Room data source." }
                database.movieDao.getMoviesByYear(filters.year)
            }
            filters.isFavorite != null -> {
                database.movieDao.getMoviesByFavorite(filters.isFavorite)
            }
            else -> {
                database.movieDao.getMovies()
            }
        }

        movieFlow.distinctUntilChanged().map { list ->
            emit(list.map { it.toDomainObject() })
        }
    }
}