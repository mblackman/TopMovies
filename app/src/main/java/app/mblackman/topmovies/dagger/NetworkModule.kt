package app.mblackman.topmovies.dagger

import app.mblackman.topmovies.data.network.MovieAdapter
import app.mblackman.topmovies.data.network.localhost.ApiMovieAdapter
import app.mblackman.topmovies.data.network.localhost.ApiService
import app.mblackman.topmovies.data.network.localhost.createService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindMovieAdapter(movieAdapter: ApiMovieAdapter): MovieAdapter
}

@Module
@InstallIn(ActivityComponent::class)
object ExternalNetworkModule {

    @Provides
    fun provideApiService(): ApiService = createService(ApiService::class.java)
}