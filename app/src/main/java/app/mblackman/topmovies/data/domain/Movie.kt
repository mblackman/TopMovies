package app.mblackman.topmovies.data.domain

import java.time.LocalDate

/**
 * Represents a movie and its associated data.
 */
data class Movie(
    val id: Long = 0,
    val title: String? = null,
    val year: Int? = null,
    val genres: List<String> = emptyList(),
    val languages: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val ratings: List<Rating> = emptyList(),
    val releaseDate: LocalDate? = null,
    val runtime: Int? = null,
    val director: String? = null,
    val actors: List<String> = emptyList(),
    val writers: List<String> = emptyList(),
    val plotSummary: String? = null,
    val posterImgUrl: String? = null,
    val productionCompany: String? = null,
    val isFavorite: Boolean = false
)