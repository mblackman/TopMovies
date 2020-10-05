package app.mblackman.topmovies.ui.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.mblackman.topmovies.databinding.MovieDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Fragment for movie details.
 */
@AndroidEntryPoint
class MovieDetails : Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel: MovieDetailsViewModel by viewModels()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = MovieDetailsArgs.fromBundle(requireArguments())
        val binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)

        viewModel.setMovie(arguments.movieId)

        viewModel.selectedMovie.observe(viewLifecycleOwner) {
            binding.movie = it
        }

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        binding.favoriteButton.setOnClickListener {
            viewModel.selectedMovie.value?.let {movie ->
                viewModel.toggleFavoriteStatus(movie)
            }
        }

        return binding.root
    }
}