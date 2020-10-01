package app.mblackman.topmovies

import app.mblackman.topmovies.data.network.Failure
import app.mblackman.topmovies.data.network.NetworkException
import app.mblackman.topmovies.data.network.Success
import app.mblackman.topmovies.data.network.localhost.ApiMovieAdapter
import app.mblackman.topmovies.data.network.localhost.ApiService
import app.mblackman.topmovies.data.network.localhost.Movie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test

import retrofit2.Response

class ApiMovieAdapterTests {
    @Test
    fun getMoviesSuccess() = runBlocking {
        val movieTitle = "Test"
        val movieAdapter = setupMovieAdapter {
            coEvery { it.getMoviesAsync() } returns CompletableDeferred(
                Response.success(
                    listOf(
                        Movie(title = movieTitle)
                    )
                )
            )
        }

        val result = movieAdapter.getTopMovies()

        assertThat(result).isInstanceOf(Success::class.java)
        val movies = (result as Success).result
        assertThat(movies.size).isEqualTo(1)
        assertThat(movies.first().title).isEqualTo(movieTitle)
    }

    @Test
    fun getMoviesEmptyList() = runBlocking {
        val movieAdapter = setupMovieAdapter {
            coEvery { it.getMoviesAsync() } returns CompletableDeferred(Response.success(emptyList()))
        }

        val result = movieAdapter.getTopMovies()

        assertThat(result).isInstanceOf(Success::class.java)
        val movies = (result as Success).result
        assertThat(movies.size).isEqualTo(0)
    }

    @Test
    fun getMoviesEmptyResponseBody() = runBlocking {
        val movieAdapter = setupMovieAdapter {
            coEvery { it.getMoviesAsync() } returns CompletableDeferred(Response.success(null))
        }

        val result = movieAdapter.getTopMovies()

        assertThat(result).isInstanceOf(Failure::class.java)
        assertThat((result as Failure).throwable).isInstanceOf(NetworkException::class.java)
    }

    @Test
    fun getMoviesFailure() = runBlocking {
        val movieAdapter = setupMovieAdapter {
            coEvery { it.getMoviesAsync() } returns CompletableDeferred(
                Response.error(
                    404, ResponseBody.create(
                        okhttp3.MultipartBody.ALTERNATIVE, "Failure"
                    )
                )
            )
        }

        val result = movieAdapter.getTopMovies()

        assertThat(result).isInstanceOf(Failure::class.java)
        assertThat((result as Failure).throwable).isInstanceOf(NetworkException::class.java)
    }

    private fun setupMovieAdapter(func: (ApiService) -> Unit): ApiMovieAdapter {
        val apiService = mockk<ApiService>()
        func(apiService)
        return ApiMovieAdapter(apiService)
    }
}