package app.mblackman.topmovies

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.mblackman.topmovies.data.common.RatingSource
import app.mblackman.topmovies.data.database.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.runBlocking
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

    @FlowPreview
    @Test
    fun filterByMultipleGenres() = runBlocking {
        loadMovies()
       movieDatabase.movieDao.getMoviesByGenre(listOf("Drama", "War"), 2).testAction(this) {
           assertThat(it.count()).isEqualTo(1)
           is1917(it.first())
       }
    }

    @FlowPreview
    @Test
    fun filterBySingleGenres() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByGenre(listOf("Drama"), 1).testAction(this) {
            assertThat(it.count()).isEqualTo(3)
            is1917(it.first())
        }
    }

    @FlowPreview
    @Test
    fun filterByGenresNoMatches() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByGenre(listOf("Made Up Genre"), 1).testAction(this) {
            assertThat(it.count()).isEqualTo(0)
        }
    }

    @FlowPreview
    @Test
    fun filterByFavorite() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMovies().testAction(this) {
            val firstFav = it.first().movie.copy(isFavorite = true)
            movieDatabase.movieDao.updateMovie(firstFav)
        }
        movieDatabase.movieDao.getMoviesByFavorite(true).testAction(this) {
            assertThat(it.count()).isEqualTo(1)
            assertThat(it.first().movie.title).isEqualTo("1917")
        }
    }

    @FlowPreview
    @Test
    fun filterByFavoriteNoMatches() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByFavorite(true).testAction(this) {
            assertThat(it.count()).isEqualTo(0)
        }
    }

    @FlowPreview
    @Test
    fun filterByNonFavorites() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByFavorite(false).testAction(this) {
            assertThat(it.count()).isEqualTo(4)
        }
    }

    @FlowPreview
    @Test
    fun filterByGenresPartialMatch() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByGenre(listOf("Drama", "War", "Made Up Genre"), 3).testAction(this) {
            assertThat(it.count()).isEqualTo(0)
        }
    }

    @FlowPreview
    @Test
    fun filterByYear() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByYear(2019).testAction(this) {
            assertThat(it.count()).isEqualTo(4)
        }
    }

    @FlowPreview
    @Test
    fun filterByYearNoMatch() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMoviesByYear(0).testAction(this) {
            assertThat(it.count()).isEqualTo(0)
        }
    }

    @FlowPreview
    @Test
    fun setAndGetMovieDetails() = runBlocking {
        loadMovies()
        movieDatabase.movieDao.getMovies().testAction(this) {
            assertThat(it.size).isEqualTo(4)
            is1917(it.first())
        }
    }

    @FlowPreview
    private suspend fun <T> Flow<T>.testAction(scope: CoroutineScope, action: (T) -> Unit) {
        val receiver = this.produceIn(scope)
        with (receiver.receive()) {
            action(this)
        }
        receiver.cancel()
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

    private suspend fun loadMovies(filename: String = topMoviesFilename) {
        this.javaClass.classLoader?.getResource(filename)?.readText()?.let { json ->
            with(loadMovieJson(json)) {
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


}

