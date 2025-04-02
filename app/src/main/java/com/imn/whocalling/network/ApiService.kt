package com.imn.whocalling.network

import com.imn.whocalling.MockyName
import com.imn.whocalling.data.MockyResponse
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("v3/$MockyName")
    suspend fun getMockyData(): Response<MockyResponse>
}