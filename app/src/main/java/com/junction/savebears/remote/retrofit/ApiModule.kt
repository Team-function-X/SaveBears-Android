package com.junction.savebears.remote.retrofit

import com.junction.savebears.remote.api.SaveBearsApi
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideOkHttpClient
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideRetrofit
import retrofit2.Retrofit

object ApiModule {

    fun saveBearsApi(): SaveBearsApi =
        provideSaveBearsApi()
            .create(SaveBearsApi::class.java)

    private fun provideSaveBearsApi(): Retrofit =
        provideRetrofit()
            .baseUrl("https://jsonplaceholder.typicode.com") // TODO baseUrl 추가
            .client(provideOkHttpClient().build())
            .build()

}