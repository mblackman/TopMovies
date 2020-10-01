package app.mblackman.topmovies.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import app.mblackman.topmovies.data.repository.MovieRepository

/**
 * Handles updating and retrieving [Movie]s.
 */
class MainViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

}