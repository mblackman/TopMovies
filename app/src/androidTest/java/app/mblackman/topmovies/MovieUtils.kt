package app.mblackman.topmovies

import app.mblackman.topmovies.data.common.RatingSource
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.domain.Rating
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Reader
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

/**
 * loads the json data for movies from the [Reader].
 *
 * @param reader The stream to read json data from.
 * @return The list of [Movie].
 */
fun loadMovieJson(reader: Reader): List<Movie> {
    val arrayMovieType = object : TypeToken<Array<JsonMovie>>() {}.type
    val movies : Array<JsonMovie> = Gson().fromJson(reader, arrayMovieType)
    return movies.mapIndexed() { i, movie ->
        Movie(
            id = i.toLong(),
            title = movie.title,
            year =  movie.year,
            genres = movie.genres.splitInput(),
            languages = movie.languages.splitInput(),
            countries = movie.countries.splitInput(),
            ratings = movie.rating?.map { it.toDomainObject() } ?: emptyList(),
            releaseDate = movie.releaseDate?.let { LocalDate.parse(it, dateTimeFormatter) },
            runtime = movie.runtime?.split(' ')?.first()?.toInt(),
            director = movie.director,
            actors = movie.actors.splitInput(),
            writers = movie.writers.splitInput(),
            plotSummary = movie.plotSummary,
            posterImgUrl = movie.posterImgUrl,
            productionCompany = movie.productionCompany
        )
    }
}

/**
 * Splits a comma separated string into a trimmed list of strings.
 */
private fun String?.splitInput() = this?.split(',')?.map { it.trim() } ?: emptyList()

private data class JsonMovie(
    @SerializedName("Title")
    val title: String? = null,
    @SerializedName("Year")
    val year: Int? = null,
    @SerializedName("Genre")
    val genres: String? = null,
    @SerializedName("Language")
    val languages: String? = null,
    @SerializedName("Country")
    val countries: String? = null,
    @SerializedName("Ratings")
    val rating: List<JsonRating>? = null,
    @SerializedName("Released")
    val releaseDate: String? = null,
    @SerializedName("Runtime")
    val runtime: String? = null,
    @SerializedName("Director")
    val director: String? = null,
    @SerializedName("Actors")
    val actors: String? = null,
    @SerializedName("Writer")
    val writers: String? = null,
    @SerializedName("Plot")
    val plotSummary: String? = null,
    @SerializedName("Poster")
    val posterImgUrl: String? = null,
    @SerializedName("Production")
    val productionCompany: String? = null,
    val isFavorite: Boolean = false
)

private data class JsonRating(
    @SerializedName("Source")
    val source: String? = null,
    @SerializedName("Value")
    val value: String? = null
)

private fun JsonRating.toDomainObject() =
    Rating(
        source = when (this.source) {
            "Internet Movie Database" -> RatingSource.Imdb
            "Rotten Tomatoes" -> RatingSource.RottenTomatoes
            "Metacritic" -> RatingSource.Metacritic
            else -> RatingSource.None
        },
        value = this.value ?: "No rating set."
    )