package app.mblackman.topmovies.data.domain

import app.mblackman.topmovies.data.common.RatingSource

data class Rating(
    val source: RatingSource,
    val value: String
)