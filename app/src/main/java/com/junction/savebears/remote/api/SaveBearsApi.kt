package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.SampleResponse
import retrofit2.Response
import retrofit2.http.GET

interface SaveBearsApi {

    @GET("")
    suspend fun sampleApi(): Response<List<SampleResponse>>
}
