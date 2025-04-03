package com.imn.whocalling.network

import com.imn.whocalling.data.MockyResponse
import com.imn.whocalling.util.Constants
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("v3/${Constants.MOCKY_NAME}")
    suspend fun getMockyData(): Response<MockyResponse>
}