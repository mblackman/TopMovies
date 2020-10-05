package app.mblackman.topmovies.ui.main

import app.mblackman.topmovies.data.domain.Movie

/**
 * A click listener when a [Movie] is clicked in a [View].
 *
 * @param clickListener The function to run when a [Movie] is clicked.
 */
class MovieClickListener(val clickListener: (movie: Movie) -> Unit) {
    /**
     * Called whenever a [Movie] is clicked.
     */
    fun onClick(movie: Movie) = clickListener(movie)
}