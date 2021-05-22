package com.junction.savebears.remote.api

import com.junction.savebears.remote.model.ChallengeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChallengeApi {
    @GET("challenges/image-data")
    suspend fun getChallengeResult(
        @Query("image") imageName: String
    ): ChallengeResponse
}