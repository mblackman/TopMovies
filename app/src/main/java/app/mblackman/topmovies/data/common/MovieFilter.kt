package app.mblackman.topmovies.data.common

/**
 * Filters for selecting movies.
 */
data class MovieFilter(
    val genres: List<String>? = null,
    val year: Int? = null,
    val isFavorite: Boolean? = null
)