package com.orb.bmdadmin.data

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Module.kt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideNetworkMonitor(application: Application): NetworkMonitor {
        return NetworkMonitor(application)
    }
}