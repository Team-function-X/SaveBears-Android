package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.GlacierData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface SaveBearsApi {

    @GET("/glacier")
    fun getGlacierChange(): Flow<GlacierData>

    @PUT("/sample")
    fun putImageData(): Flow<Response<GlacierData>>
}
