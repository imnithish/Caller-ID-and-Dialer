package com.imn.whocalling.di

import android.content.Context
import com.imn.whocalling.network.ApiService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ApiServiceEntryPoint {
    fun getApiService(): ApiService
}

fun getApiService(context: Context): ApiService {
    val entryPoint = EntryPointAccessors.fromApplication(context, ApiServiceEntryPoint::class.java)
    return entryPoint.getApiService()
}