package app.mblackman.topmovies.ui.main

import app.mblackman.topmovies.data.domain.Movie

class MovieClickListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}