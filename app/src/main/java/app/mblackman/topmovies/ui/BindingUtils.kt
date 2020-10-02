package app.mblackman.topmovies.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import app.mblackman.topmovies.R
import app.mblackman.topmovies.data.common.MovieFilter
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
    var categoryString: String? = null

    if (filter == null) {
        categoryString = "Top Movies"
    } else {
        if (filter.isFavorite == true) {
            categoryString += "Favorite"
        }
        if (filter.year != null) {
            if (categoryString != null) {
                categoryString += ": "
            }

            categoryString += "Release Year ${filter.year}"
        }
        if (!filter.genres.isNullOrEmpty()) {
            if (categoryString != null) {
                categoryString += " - "
            }

            categoryString += filter.genres.joinToString(separator = ", ", prefix = "Genres: ")
        }
    }

    textView.text = categoryString
}