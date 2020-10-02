package app.mblackman.topmovies.data.network.localhost

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "http://192.168.50.82:4000"

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
        .baseUrl(BASE_URL)
        .build()

    return retrofit.create(serviceClass)
}