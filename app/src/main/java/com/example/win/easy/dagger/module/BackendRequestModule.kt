package com.example.win.easy.dagger.module

import com.example.win.easy.network.BackendRequestService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
object BackendRequestModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideBackendResourceWebService(@Named("serverUrl") serverUrl: String): BackendRequestService {
        val retrofit = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(BackendRequestService::class.java)
    }

    @JvmStatic
    @Provides
    @Named("serverUrl")
    fun provideServerUrl() = "http://10.0.2.2:9000/"
}