package app.mblackman.topmovies.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import app.mblackman.topmovies.R
import app.mblackman.topmovies.data.common.MovieFilter
import app.mblackman.topmovies.data.common.RatingSource
import app.mblackman.topmovies.data.domain.Movie
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Provides a binding to load an image url with Glide. This handles showing a loading icon
 * and error icon.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.hourglass_top_24px)
                    .error(R.drawable.broken_image_24px)
            )
            .into(imgView)
    }
}

@BindingAdapter("movieCategory")
fun bindMovieCategory(textView: TextView, filter: MovieFilter?) {
    var categoryString: String = ""

    if (filter == null) {
        categoryString = "Top Movies"
    } else {
        if (filter.isFavorite == true) {
            categoryString += "Favorite"
        }
        if (filter.year != null) {
            if (categoryString != "") {
                categoryString += ": "
            }

            categoryString += "Release Year: ${filter.year}"
        }
        if (!filter.genres.isNullOrEmpty()) {
            if (categoryString != "") {
                categoryString += " - "
            }

            categoryString += filter.genres.joinToString(separator = ", ", prefix = "Genres: ")
        }
    }

    textView.text = categoryString
}

@BindingAdapter("movieRatings")
fun bindMovieRatings(textView: TextView, movie: Movie?) {
    movie?.ratings?.firstOrNull { it.source == RatingSource.Imdb }?.let {
        textView.text = "IMDB: ${it.value}"
    }
}

@BindingAdapter("delimitedList")
fun bindDelimitedList(textView: TextView, items: List<String>?) {
    items?.let {
        textView.text = items.joinToString(separator = ", ")
    }
}

@BindingAdapter("movieRuntime")
fun bindMovieRuntime(textView: TextView, movie: Movie?) {
    if (movie?.runtime != null) {
        textView.text = "${movie.runtime.toString()} min"
    }
}