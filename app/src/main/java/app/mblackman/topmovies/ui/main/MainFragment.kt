package app.mblackman.topmovies.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private val defaultFilters = listOf(
        MovieFilter(isFavorite = true),
        MovieFilter(year = 2019),
        MovieFilter(year = 2018),
        MovieFilter(genres = listOf("Mystery", "Drama")),
        MovieFilter(genres = listOf("Action")),
        MovieFilter(genres = listOf("Fantasy")),
        MovieFilter(genres = listOf("Thriller"))
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val adapter = MovieCategoryListAdapter(
            lifecycleOwner =  viewLifecycleOwner,
            movieClickListener =  MovieClickListener {
            openMovieDetails(it)
            },
            favoriteClickListener = MovieClickListener {
                viewModel.toggleFavoriteStatus(it)
            }
        )

        val movieCategoryList = defaultFilters.map { viewModel.getMovieCategoryList(it) }

        adapter.submitList(movieCategoryList)
        binding.moviesCarouselList.layoutManager = layoutManager
        binding.moviesCarouselList.adapter = adapter

        return binding.root
    }

    private fun openMovieDetails(movie: Movie) {
        this.findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToMovieDetails(movie.id)
        )
    }
}