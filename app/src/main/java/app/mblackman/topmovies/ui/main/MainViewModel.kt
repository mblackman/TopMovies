package app.mblackman.topmovies.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.repository.MovieRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOn

/**
 * Handles updating and retrieving [Movie]s.
 */
class MainViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    init {
        getTopMovies()
    }

    fun getTopMovies() {
        viewModelScope.launch {
            movieRepository.getUpdatedMovies()
        }
    }

    fun getMovieCategoryList(filter: MovieFilter? = null) =
        MovieCategoryList(filter, getMovies(filter))

    fun getMovies(filter: MovieFilter? = null): LiveData<List<Movie>> =
        this.movieRepository
            .getMovies(filter)
            .asLiveData()
}