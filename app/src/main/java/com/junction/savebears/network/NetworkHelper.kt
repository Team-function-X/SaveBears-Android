package com.junction.savebears.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

object NetworkHelper {
    private const val serverBaseUrl = "https://"

    var token: String = ""

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })

        .addInterceptor {
            // Request
            val request = it.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            Timber.d("[OkHTTP] request: ${it.request()}")
            Timber.d("[OkHTTP] request header: ${it.request().headers}")

            // Response
            val response = it.proceed(request)

            Timber.d("[OkHTTP] response : $response")
            Timber.d("[OkHTTP] response header: ${response.headers}")
            response
        }.build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(serverBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)

}
