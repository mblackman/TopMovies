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

@ExperimentalCoroutinesApi
class MovieDetailsViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val selectedMovieId = ConflatedBroadcastChannel<Long>()

    @FlowPreview
    val selectedMovie: LiveData<Movie?> = selectedMovieId.asFlow()
        .flatMapLatest {
            this.movieRepository.getMovie(it)
        }.asLiveData()

    fun setMovie(id: Long) {
        selectedMovieId.offer(id)
    }

    fun toggleFavoriteStatus(movie: Movie?) {
        movie ?: return
        viewModelScope.launch {
            movieRepository.toggleFavoriteStatus(movie)
        }
    }
}