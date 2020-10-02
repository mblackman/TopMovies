package app.mblackman.topmovies.ui.main

import androidx.lifecycle.LiveData
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie

/**
 * Holds a filter and the movies that filter retrieved.
 */
data class MovieCategoryList(
    val filter: MovieFilter? = null,
    val movies: LiveData<List<Movie>>
)