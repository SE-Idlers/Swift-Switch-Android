package com.example.win.easy.dagger.module

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import javax.inject.Named
import javax.inject.Singleton

@Module
object ThreadModule {

    @ObsoleteCoroutinesApi
    @JvmStatic
    @Provides
    @Singleton
    fun provideDownloadDispatcher() = newSingleThreadContext("DownloadThread")
}