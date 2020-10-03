package app.mblackman.topmovies.ui

import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.repository.MovieRepository

/**
 * Toggles the favorite status on the given movie and updates the storage.
 *
 * @param movie The movie to update.
 */
fun MovieRepository.toggleFavoriteStatus(movie: Movie) {
    val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
    this.updateMovie(updatedMovie)
}