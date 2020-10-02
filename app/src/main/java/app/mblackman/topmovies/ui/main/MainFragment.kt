package app.mblackman.topmovies.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.topmovies.R
import app.mblackman.topmovies.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.movie_carousel.view.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MovieListAdapter(viewLifecycleOwner)

        val movieCategoryList = viewModel.getMovieCategoryList()

        movieCategoryList.movies.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.carousel.movies.layoutManager = layoutManager
        binding.carousel.movies.adapter = adapter

        binding.carousel.movieCategoryList = movieCategoryList

        return binding.root
    }
}