package app.mblackman.topmovies.data.domain

import app.mblackman.topmovies.data.common.RatingSource

/**
 * Represents a rating from a source with it's details.
 */
data class Rating(
    val source: RatingSource,
    val value: String
)