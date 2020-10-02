package app.mblackman.topmovies.ui.moviedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.mblackman.topmovies.R
import app.mblackman.topmovies.databinding.MovieDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetails : Fragment() {

    private val viewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val arguments = MovieDetailsArgs.fromBundle(requireArguments())
        val binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)

        viewModel.getMovieDetails(arguments.movieId).observe(viewLifecycleOwner) {
            binding.movie = it
        }

        binding.backButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }
}