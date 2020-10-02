package app.mblackman.topmovies.data.network.localhost

import app.mblackman.topmovies.splitInput
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

/**
 * A movie and details from the Movie API.
 */
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "Title")
    val title: String? = null,
    @Json(name = "Year")
    val year: Int? = null,
    @Json(name = "Rated")
    val rated: String? = null,
    @Json(name = "Genre")
    val genres: String? = null,
    @Json(name = "Language")
    val languages: String? = null,
    @Json(name = "Country")
    val countries: String? = null,
    @Json(name = "Ratings")
    val rating: List<Rating>? = null,
    @Json(name = "Released")
    val releaseDate: String? = null,
    @Json(name = "Runtime")
    val runtime: String? = null,
    @Json(name = "Director")
    val director: String? = null,
    @Json(name = "Actors")
    val actors: String? = null,
    @Json(name = "Writer")
    val writers: String? = null,
    @Json(name = "Plot")
    val plotSummary: String? = null,
    @Json(name = "Poster")
    val posterImgUrl: String? = null,
    @Json(name = "Production")
    val productionCompany: String? = null
)

/**
 * Converts the [Movie] to a domain [Movie].
 */
fun Movie.toDomainObject(id: Long) =
    app.mblackman.topmovies.data.domain.Movie(
        id = id,
        title = this.title,
        year = this.year,
        rated = this.rated,
        genres = this.genres.splitInput(),
        languages = this.languages.splitInput(),
        countries = this.countries.splitInput(),
        ratings = this.rating?.map { it.toDomainObject() } ?: emptyList(),
        releaseDate = this.releaseDate?.let { LocalDate.parse(it, dateTimeFormatter) },
        runtime = this.runtime?.split(' ')?.first()?.toInt(),
        director = this.director,
        actors = this.actors.splitInput(),
        writers = this.writers.splitInput(),
        plotSummary = this.plotSummary,
        posterImgUrl = this.posterImgUrl,
        productionCompany = this.productionCompany
    )