package com.example.win.easy.dagger.module

import com.example.win.easy.web.request.BackendRequestService
import com.example.win.easy.web.service.LoginService
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