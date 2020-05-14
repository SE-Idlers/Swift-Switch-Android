package com.example.win.easy.dagger.module

import com.example.win.easy.network.BackendRequestService
import com.example.win.easy.network.LoginService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LoginModule {

    @JvmStatic
    @Provides
    @Singleton
    fun providesLoginService(backendRequestService: BackendRequestService): LoginService = LoginService(backendRequestService)
}