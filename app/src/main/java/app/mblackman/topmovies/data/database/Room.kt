package app.mblackman.topmovies.data.database

import androidx.room.*
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

/**
 * [Dao] interface to define methods to interact with [Movie] in a room database.
 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<Movie>)

    @Transaction
    fun insertMovieWithDetails(movies: List<MovieWithDetails>) {
        insertMovies(movies.map { it.movie })

        movies.forEach { movie ->
            insertRatings(movie.ratings)
            insertGenres(movie.genres)
            insertWriters(movie.writers)
            insertActors(movie.actors)
            insertLanguages(movie.languages)
            insertCountries(movie.countries)
        }
    }

    /**
     * Updates the given [Movie].
     */
    @Update
    fun updateMovie(movie: Movie)

    /**
     * Gets the movie with the given id.
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE id = :id LIMIT 1")
    fun getMovie(id: Long): Flow<MovieWithDetails?>

    /**
     * Gets all the [Movie]s with additional details.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie")
    fun getMovies(): Flow<List<MovieWithDetails>>

    /**
     * Gets all the movies with all given [Genre]s.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE id in (SELECT movieId FROM Genre WHERE genre.name IN(:genres) GROUP BY movieId HAVING COUNT(1) = :genreCount)")
    fun getMoviesByGenre(genres: List<String>, genreCount: Int): Flow<List<MovieWithDetails>>

    /**
     * Gets all the movies release in the given year.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE year = :year")
    fun getMoviesByYear(year: Int): Flow<List<MovieWithDetails>>

    /**
     * Gets all the movies with the given favorite status.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE isFavorite = :isFavorite")
    fun getMoviesByFavorite(isFavorite: Boolean): Flow<List<MovieWithDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRatings(ratings: List<Rating>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGenres(genres: List<Genre>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWriters(writers: List<Writer>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActors(actors: List<Actor>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLanguages(languages: List<Language>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCountries(countries: List<Country>)
}

/**
 * The database containing all the data for the movies.
 */
@Database(
    entities = [Movie::class, Rating::class, Genre::class, Writer::class, Actor::class, Language::class, Country::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    /**
     * The [Dao] to interact with [Movie].
     */
    abstract val movieDao: MovieDao
}
