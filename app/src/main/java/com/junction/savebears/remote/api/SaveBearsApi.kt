package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.GlacierResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT

interface SaveBearsApi {

    @GET("/glacier")
    suspend fun getGlacierChange(): GlacierResponse

    @PUT("/sample")
    suspend fun putImageData(): Response<GlacierResponse>
}
