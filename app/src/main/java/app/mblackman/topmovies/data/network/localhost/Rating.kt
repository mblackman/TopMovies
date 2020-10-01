package app.mblackman.topmovies.data.network.localhost

import app.mblackman.topmovies.data.common.RatingSource
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A rating for a [Movie] from the API.
 */
@JsonClass(generateAdapter = true)
data class Rating(
    @Json(name = "Source")
    val source: String? = null,
    @Json(name = "Value")
    val value: String? = null
)

/**
 * Converts the [Rating] to a domain [Rating].
 */
fun Rating.toDomainObject() =
    app.mblackman.topmovies.data.domain.Rating(
        source = when (this.source) {
            "Internet Movie Database" -> RatingSource.Imdb
            "Rotten Tomatoes" -> RatingSource.RottenTomatoes
            "Metacritic" -> RatingSource.Metacritic
            else -> RatingSource.None
        },
        value = this.value ?: "No rating set."
    )