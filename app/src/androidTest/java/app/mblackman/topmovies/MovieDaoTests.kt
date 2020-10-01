package app.mblackman.topmovies

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.mblackman.topmovies.data.common.RatingSource
import app.mblackman.topmovies.data.database.*
import com.google.common.truth.Truth.assertThat
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.io.IOException
import java.time.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MovieDaoTests {
    private val topMoviesFilename = "movies.json"

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
    fun filterByGenre() {
        loadMovies()
        val movies = movieDatabase.movieDao.getMoviesByGenre(listOf("Drama", "War"), 2)
        assertThat(movies.count()).isEqualTo(1)
        is1917(movies.first())
    }

    @Test
    fun setAndGetMovieDetails() {
        loadMovies()
        val movies = movieDatabase.movieDao.getMovies()
        assertThat(movies.size).isEqualTo(4)
        is1917(movies.first())
    }

    private fun is1917(movie: MovieWithDetails) {
        val movieId = 0L
        val expectedMovie = Movie(
            id = movieId,
            title = "1917",
            year = 2019,
            releaseDate = LocalDate.of(2020, 1, 10),
            runtime = 119,
            director = "Sam Mendes",
            plotSummary = "April 6th, 1917. As a regiment assembles to wage war deep in enemy territory, two soldiers are assigned to race against time and deliver a message that will stop 1,600 men from walking straight into a deadly trap.",
            posterImgUrl = "https://m.media-amazon.com/images/M/MV5BOTdmNTFjNDEtNzg0My00ZjkxLTg1ZDAtZTdkMDc2ZmFiNWQ1XkEyXkFqcGdeQXVyNTAzNzgwNTg@._V1_SX300.jpg",
            type = "movie",
            productionCompany = "Universal Pictures",
            isFavorite = false
        )
        val genres = listOf(Genre("Drama", movieId), Genre("War", movieId))
        val ratings = listOf(
            Rating(RatingSource.Imdb, "8.5/10", movieId),
            Rating(RatingSource.RottenTomatoes, "89%", movieId),
            Rating(RatingSource.Metacritic, "78/100", movieId)
        )
        val writers = listOf(Writer("Sam Mendes", movieId), Writer("Krysty Wilson-Cairns", movieId))
        val actors = listOf(Actor("Dean-Charles Chapman", movieId), Actor("George MacKay", movieId))
        val languages = listOf(
            Language("English", movieId),
            Language("French", movieId),
            Language("German", movieId)
        )
        val countries = listOf(Country("USA", movieId), Country("UK", movieId))

        assertThat(movie.movie).isEqualTo(expectedMovie)
        assertThat(movie.genres).isEqualTo(genres)
        assertThat(movie.ratings).isEqualTo(ratings)
        assertThat(movie.writers).isEqualTo(writers)
        assertThat(movie.actors).isEqualTo(actors)
        assertThat(movie.languages).isEqualTo(languages)
        assertThat(movie.countries).isEqualTo(countries)
    }

    private fun loadMovies(filename: String = topMoviesFilename) {
        this.javaClass.classLoader?.getResourceAsStream(filename)?.bufferedReader()?.use { reader ->
            with(loadMovieJson(reader)) {
                movieDatabase.movieDao.insertAll(this.map { it.toDatabaseObject() })

                forEach { movie ->
                    movieDatabase.ratingDao.insertAll(movie.ratings.map { it.toDatabaseObject(movie.id) })
                    movieDatabase.genreDao.insertAll(movie.genres.map { Genre(it, movie.id) })
                    movieDatabase.writerDao.insertAll(movie.writers.map { Writer(it, movie.id) })
                    movieDatabase.actorDao.insertAll(movie.actors.map { Actor(it, movie.id) })
                    movieDatabase.languageDao.insertAll(movie.languages.map {
                        Language(
                            it,
                            movie.id
                        )
                    })
                    movieDatabase.countryDao.insertAll(movie.countries.map {
                        Country(
                            it,
                            movie.id
                        )
                    })
                }
            }
        }
    }

    private fun app.mblackman.topmovies.data.domain.Movie.toDatabaseObject(): Movie =
        Movie(
            this.id,
            this.title,
            this.year,
            this.releaseDate,
            this.runtime,
            this.director,
            this.plotSummary,
            this.posterImgUrl,
            "movie",
            this.productionCompany,
            this.isFavorite
        )

    private fun app.mblackman.topmovies.data.domain.Rating.toDatabaseObject(movieId: Long): Rating =
        Rating(
            this.source,
            this.value,
            movieId
        )
}

