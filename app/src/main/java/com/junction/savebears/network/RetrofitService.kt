package com.junction.savebears.network

import com.junction.savebears.data.response.GlacierInfo
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {

    /**
     * 빙하 변화량 (용해량) 정보를 얻는 API
     */
    @GET("/glacier/")
    fun getGlacierChange(): Call<GlacierInfo>
}