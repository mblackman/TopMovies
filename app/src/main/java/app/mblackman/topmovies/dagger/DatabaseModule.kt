package app.mblackman.topmovies.dagger

import android.content.Context
import androidx.room.Room
import app.mblackman.topmovies.data.database.MovieStore
import app.mblackman.topmovies.data.database.room.MovieDatabase
import app.mblackman.topmovies.data.database.room.RoomMovieStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "MovieDatabase"
        ).build()
}

@Module
@InstallIn(ActivityComponent::class)
abstract class StoreModule {

    @Binds
    abstract fun bindMovieStore(movieStore: RoomMovieStore): MovieStore
}