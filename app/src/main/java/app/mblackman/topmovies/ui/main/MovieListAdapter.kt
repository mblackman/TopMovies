package app.mblackman.topmovies.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.databinding.MovieDetailsSmallBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * An adapter for order movies. Helps bind the order properties with
 * view items in a collection.
 *
 * @param lifecycleOwner The lifecycle owner for the owner of this adapter.
 */
class MovieListAdapter(
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<Movie, RecyclerView.ViewHolder>(diffCallback) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val _movieClicked = MutableLiveData<Movie>()

    val movieClicked: LiveData<Movie>
        get() = _movieClicked

    /**
     * Checks for differences between movies.
     */
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BindingViewHolder -> {
                val item = getItem(position) as Movie
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieDetailsSmallBinding.inflate(layoutInflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int = 0

    /**
     * View Holder for the order items being created. Handles binding the views to the orders.
     */
    inner class BindingViewHolder(private val binding: MovieDetailsSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the view to the given order.
         *
         * @param item The order to bind to.
         */
        fun bind(item: Movie) {
            binding.movie = item
            binding.lifecycleOwner = lifecycleOwner
            binding.executePendingBindings()
        }
    }
}