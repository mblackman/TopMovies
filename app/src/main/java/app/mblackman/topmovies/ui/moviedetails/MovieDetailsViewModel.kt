package app.mblackman.topmovies.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.repository.MovieRepository
import app.mblackman.topmovies.ui.toggleFavoriteStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Handles getting and updating a movie.
 */
@ExperimentalCoroutinesApi
class MovieDetailsViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val selectedMovieId = ConflatedBroadcastChannel<Long>()

    /**
     * The [Movie] currently selected on this view model.
     */
    @FlowPreview
    val selectedMovie: LiveData<Movie?> = selectedMovieId.asFlow()
        .flatMapLatest {
            this.movieRepository.getMovie(it)
        }.asLiveData()

    /**
     * Sets the movie on this [ViewModel].
     *
     * @param id The id of the [Movie].
     */
    fun setMovie(id: Long) {
        selectedMovieId.offer(id)
    }

    /**
     * Toggles the favorite status of the given [Movie].
     *
     * @param movie The [Movie] to toggle favorite status for.
     */
    fun toggleFavoriteStatus(movie: Movie?) {
        movie ?: return
        viewModelScope.launch {
            movieRepository.toggleFavoriteStatus(movie)
        }
    }
}