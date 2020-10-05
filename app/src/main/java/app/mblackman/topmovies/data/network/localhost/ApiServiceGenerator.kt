package app.mblackman.topmovies.data.network.localhost

import app.mblackman.topmovies.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Create a service.
 *
 * @param serviceClass The class to generate the service from.
 * @return The new service.
 */
fun <T> createService(serviceClass: Class<T>): T {
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BuildConfig.MOVIES_URL)
        .build()

    return retrofit.create(serviceClass)
}