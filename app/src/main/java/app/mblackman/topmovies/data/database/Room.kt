package app.mblackman.topmovies.data.database

import androidx.room.*
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

/**
 * Defines base members for [Dao]s.
 */
interface BaseDao<T> {
    /**
     * Inserts the given [T] into the dao.
     *
     * @param item The item to insert.
     *
     * @return The row id of the inserted item.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: T) : Long

    /**
     * Inserts all the [T] into the dao.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(items: List<T>)
}

/**
 * [Dao] interface to define methods to interact with [Movie] in a room database.
 */
@Dao
interface MovieDao : BaseDao<Movie> {
    /**
     * Gets all the [Movie]s with additional details.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie")
    fun getMovies(): List<MovieWithDetails>

    /**
     * Gets all the movies with all given [Genre]s.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE id in (SELECT movieId FROM Genre WHERE genre.name IN(:genres) GROUP BY movieId HAVING COUNT(1) = :genreCount)")
    fun getMoviesByGenre(genres: List<String>, genreCount: Int): List<MovieWithDetails>

    /**
     * Gets all the movies release in the given year.
     *
     * @return The list of [MovieWithDetails].
     */
    @Transaction
    @Query("SELECT * FROM Movie WHERE year = :year")
    fun getMoviesByYear(year: Int): List<MovieWithDetails>
}

/**
 * [Dao] interface to define methods to interact with [Rating] in a room database.
 */
@Dao
interface RatingDao : BaseDao<Rating> {

}

/**
 * [Dao] interface to define methods to interact with [Genre] in a room database.
 */
@Dao
interface GenreDao : BaseDao<Genre> {

}

/**
 * [Dao] interface to define methods to interact with [Writer] in a room database.
 */
@Dao
interface WriterDao : BaseDao<Writer> {

}

/**
 * [Dao] interface to define methods to interact with [Actor] in a room database.
 */
@Dao
interface ActorDao : BaseDao<Actor> {

}

/**
 * [Dao] interface to define methods to interact with [Language] in a room database.
 */
@Dao
interface LanguageDao : BaseDao<Language> {

}

/**
 * [Dao] interface to define methods to interact with [Country] in a room database.
 */
@Dao
interface CountryDao : BaseDao<Country> {

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

    /**
     * The [Dao] to interact with [Rating]
     */
    abstract val ratingDao: RatingDao

    /**
     * The [Dao] to interact with [GenreDao]
     */
    abstract val genreDao: GenreDao

    /**
     * The [Dao] to interact with [WriterDao]
     */
    abstract val writerDao: WriterDao

    /**
     * The [Dao] to interact with [ActorDao]
     */
    abstract val actorDao: ActorDao

    /**
     * The [Dao] to interact with [LanguageDao]
     */
    abstract val languageDao: LanguageDao

    /**
     * The [Dao] to interact with [CountryDao]
     */
    abstract val countryDao: CountryDao
}
