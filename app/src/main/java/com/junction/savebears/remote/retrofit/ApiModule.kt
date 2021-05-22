package com.junction.savebears.remote.retrofit

import com.junction.savebears.remote.api.SaveBearsApi
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideOkHttpClient
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideRetrofit

object ApiModule {

    private const val GLACIER_BASE_URL = "https://b7r6884n9b.execute-api.ap-northeast-2.amazonaws.com/"

    fun saveBearsApi(): SaveBearsApi =
        provideRetrofit()
            .baseUrl(GLACIER_BASE_URL)
            .client(provideOkHttpClient().build())
            .build()
            .create(SaveBearsApi::class.java)
}