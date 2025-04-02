package com.imn.whocalling.di

import com.imn.whocalling.network.ApiService
import com.imn.whocalling.ui.screens.calllog.data.CallLogRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideCallLogRepo(apiService: ApiService): CallLogRepo = CallLogRepo(apiService)
}