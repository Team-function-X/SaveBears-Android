package com.junction.savebears.remote.retrofit

import com.junction.savebears.remote.api.ChallengeApi
import com.junction.savebears.remote.api.SaveBearsApi
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideOkHttpClient
import com.junction.savebears.remote.retrofit.RemoteNetworkModule.provideRetrofit

object ApiModule {

    fun saveBearsApi(): SaveBearsApi =
        provideRetrofit()
            .baseUrl("https://google.com")
            .client(provideOkHttpClient().build())
            .build()
            .create(SaveBearsApi::class.java)

    fun challengeApi(): ChallengeApi =
        provideRetrofit()
            .baseUrl("http://ec2-15-164-220-118.ap-northeast-2.compute.amazonaws.com")
            .client(provideOkHttpClient().build())
            .build()
            .create(ChallengeApi::class.java)


}