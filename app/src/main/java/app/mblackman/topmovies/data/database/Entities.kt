package app.mblackman.topmovies.data.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import app.mblackman.topmovies.data.common.RatingSource
import java.time.LocalDate

/**
 * The basic information held about a movie.
 */
@Entity
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val title: String? = null,
    val year: Int? = null,
    val releaseDate: LocalDate? = null,
    val runtime: Int? = null,
    val director: String? = null,
    val plotSummary: String? = null,
    val posterImgUrl: String? = null,
    val productionCompany: String? = null,
    val isFavorite: Boolean = false
)

/**
 * A genre for a movie.
 */
@Entity(primaryKeys = ["name", "movieId"])
data class Genre(
    val name: String,
    val movieId: Long
)

/**
 * A writer for a movie.
 */
@Entity(primaryKeys = ["name", "movieId"])
data class Writer(
    val name: String,
    val movieId: Long
)

/**
 * An actor for a movie.
 */
@Entity(primaryKeys = ["name", "movieId"])
data class Actor(
    val name: String,
    val movieId: Long
)

/**
 * A supported language for a movie.
 */
@Entity(primaryKeys = ["name", "movieId"])
data class Language(
    val name: String,
    val movieId: Long
)

/**
 * A country the a movie played in.
 */
@Entity(primaryKeys = ["name", "movieId"])
data class Country(
    val name: String,
    val movieId: Long
)

/**
 * A rating for a movie from an authoritative source.
 */
@Entity(primaryKeys = ["source", "movieId"])
data class Rating(
    val source: RatingSource,
    val value: String,
    val movieId: Long
)

/**
 * A [Movie] with all details from related tables.
 */
data class MovieWithDetails(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val genres: List<Genre>,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val ratings: List<Rating>,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val writers: List<Writer>,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val actors: List<Actor>,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val languages: List<Language>,
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val countries: List<Country>
)