package app.mblackman.topmovies.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.topmovies.databinding.MovieCarouselBinding

/**
 * An adapter for movie categories. Helps bind the order properties with
 * view items in a collection.
 *
 * @param lifecycleOwner The lifecycle owner for the owner of this adapter.
 */
class MovieCategoryListAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val movieClickListener: MovieClickListener,
    private val favoriteClickListener: MovieClickListener
) : ListAdapter<MovieCategoryList, RecyclerView.ViewHolder>(diffCallback) {
    /**
     * Checks for differences between movie categories.
     */
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MovieCategoryList>() {
            override fun areItemsTheSame(oldItem: MovieCategoryList, newItem: MovieCategoryList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MovieCategoryList, newItem: MovieCategoryList): Boolean {
                return oldItem.filter == newItem.filter
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BindingViewHolder -> {
                val item = getItem(position) as MovieCategoryList
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieCarouselBinding.inflate(layoutInflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int = 0

    /**
     * View Holder for the movie category items being created. Handles binding the views to the movie category.
     */
    inner class BindingViewHolder(private val binding: MovieCarouselBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the view to the given movie category.
         *
         * @param item The order to bind to.
         */
        fun bind(item: MovieCategoryList) {
            binding.movieCategoryList = item

            val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = MovieListAdapter(lifecycleOwner, movieClickListener, favoriteClickListener)

            item.movies.observe(lifecycleOwner) {
                adapter.submitList(it)
            }

            binding.movies.layoutManager = layoutManager
            binding.movies.adapter = adapter
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }
}