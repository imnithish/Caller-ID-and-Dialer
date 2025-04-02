package com.imn.whocalling.ui.screens.calllog.data

import com.imn.whocalling.data.MockyResponse
import com.imn.whocalling.network.ApiService
import com.imn.whocalling.util.ResultWrapper
import com.imn.whocalling.util.safeApiCall
import retrofit2.Response

class CallLogRepo(val apiService: ApiService) {
    suspend fun getMockyData(): ResultWrapper<Response<MockyResponse>> {
        return safeApiCall { apiService.getMockyData() }
    }
}