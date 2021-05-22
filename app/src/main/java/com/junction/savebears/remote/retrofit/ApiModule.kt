package com.junction.savebears.remote.retrofit

import com.junction.savebears.remote.api.SaveBearsApi
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideOkHttpClient
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideRetrofit

object ApiModule {

    fun saveBearsApi(): SaveBearsApi =
        provideRetrofit()
            .baseUrl("https://google.com") // TODO baseUrl 추가
            .client(provideOkHttpClient().build())
            .build()
            .create(SaveBearsApi::class.java)

}