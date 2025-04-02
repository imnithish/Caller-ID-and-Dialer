package com.imn.whocalling.network

import com.imn.whocalling.data.MockyResponse
import retrofit2.http.GET
import retrofit2.Response


interface ApiService {
    @GET("v3/2d03e9f6-6ead-4c00-8f64-9ac8dd5b4143")
    suspend fun getMockyData(): Response<MockyResponse>
}