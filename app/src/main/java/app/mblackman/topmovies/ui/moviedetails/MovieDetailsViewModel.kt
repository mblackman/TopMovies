package app.mblackman.topmovies.ui.moviedetails

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import app.mblackman.topmovies.data.database.MovieWithDetails
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.repository.MovieRepository

class MovieDetailsViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    fun getMovieDetails(id: Long) =
        this.movieRepository.getMovie(id).asLiveData()

}