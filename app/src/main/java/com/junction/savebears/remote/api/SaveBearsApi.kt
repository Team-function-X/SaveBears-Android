package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.GlacierResponse
import retrofit2.http.GET

interface SaveBearsApi {
    @GET("/api")
    suspend fun getGlacierChangeData(): GlacierResponse
}
