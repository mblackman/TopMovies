package app.mblackman.topmovies

import app.mblackman.topmovies.data.domain.Movie
import app.mblackman.topmovies.data.network.localhost.toDomainObject
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import app.mblackman.topmovies.data.network.localhost.Movie as WebMovie
import java.io.Reader
/**
 * loads the json data for movies from the [Reader].
 *
 * @param json The string containing the json data.
 * @return The list of [Movie].
 */
fun loadMovieJson(json: String): List<Movie> {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val arrayMovieType = Types.newParameterizedType(List::class.java, WebMovie::class.java)
    val jsonAdapter = moshi.adapter<List<WebMovie>>(arrayMovieType)

    val movies : List<WebMovie> = jsonAdapter.fromJson(json) ?: emptyList()
    return movies.mapIndexed() { i, movie -> movie.toDomainObject(i.toLong()) }
}