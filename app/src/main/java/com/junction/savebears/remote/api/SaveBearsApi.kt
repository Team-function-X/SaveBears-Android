package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.GlacierData
import retrofit2.Call
import retrofit2.http.GET

interface SaveBearsApi {

    @GET("/glacier")
    suspend fun getGlacierChange(): Call<GlacierData>
}
