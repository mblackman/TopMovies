package app.mblackman.topmovies

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.mblackman.topmovies.data.database.*
import com.google.common.truth.Truth.assertThat
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MovieDaoTests {

    private lateinit var movieDatabase: MovieDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        movieDatabase = Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        movieDatabase.close()
    }

    @Test
    fun getDetails() {
        val movie = Movie(title = "Test")
        val movieId: Long = movieDatabase.movieDao.insert(movie)
        val genres = listOf(Genre("Horror", movieId), Genre("Comedy", movieId))
        val ratings = listOf(Rating("IMDB", "5", movieId), Rating("RT", "5", movieId))
        val writers = listOf(Writer("Test Eastwood", movieId), Writer("Test Taratino", movieId))
        val actors = listOf(Actor("Uma Test", movieId), Actor("Robert Testy Jr.", movieId))
        val languages = listOf(Language("English", movieId), Language("French", movieId))
        val countries = listOf(Country("USA", movieId), Country("France", movieId))

        movieDatabase.genreDao.insertAll(genres)
        movieDatabase.ratingDao.insertAll(ratings)
        movieDatabase.writerDao.insertAll(writers)
        movieDatabase.actorDao.insertAll(actors)
        movieDatabase.languageDao.insertAll(languages)
        movieDatabase.countryDao.insertAll(countries)

        val movies = movieDatabase.movieDao.getMovies()

        assertThat(movies.size).isEqualTo(1)
        val resultMovie = movies.first()

        assertThat(resultMovie.movie).isEqualTo(movie)
        assertThat(resultMovie.genres).isEqualTo(genres)
        assertThat(resultMovie.ratings).isEqualTo(ratings)
        assertThat(resultMovie.writers).isEqualTo(writers)
        assertThat(resultMovie.actors).isEqualTo(actors)
        assertThat(resultMovie.languages).isEqualTo(languages)
        assertThat(resultMovie.countries).isEqualTo(countries)
    }
}