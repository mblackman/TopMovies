package app.mblackman.topmovies.ui.main

import android.net.Network
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import app.mblackman.topmovies.data.common.Failure
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.NetworkException
import app.mblackman.topmovies.data.repository.MovieRepository
import app.mblackman.topmovies.ui.toggleFavoriteStatus
import kotlinx.coroutines.launch

/**
 * Handles updating and retrieving [Movie]s.
 */
class MainViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val _toastMessage = MutableLiveData<String?>()

    val toastMessage: LiveData<String?>
        get() = _toastMessage

    init {
        getTopMovies()
    }

    private fun getTopMovies() {
        viewModelScope.launch {
            when (val result = movieRepository.getUpdatedMovies()) {
                is Failure -> {
                    Log.e(this.javaClass.name, "Failed to get and update movies.", result.throwable)

                    if (result.throwable is NetworkException) {
                        _toastMessage.postValue("Failed to get latest movies!")
                    }
                }
            }
        }
    }

    fun getMovieCategoryList(filter: MovieFilter? = null) =
        MovieCategoryList(filter, getMovies(filter))

    private fun getMovies(filter: MovieFilter? = null): LiveData<List<Movie>> =
        this.movieRepository
            .getMovies(filter)
            .asLiveData()

    fun toggleFavoriteStatus(movie: Movie) {
        viewModelScope.launch {
            movieRepository.toggleFavoriteStatus(movie)
        }
    }

    fun toastHandled() {
        _toastMessage.postValue(null)
    }
}