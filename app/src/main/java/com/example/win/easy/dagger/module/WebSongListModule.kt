package com.example.win.easy.dagger.module

import com.example.win.easy.repository.SongListDto
import com.example.win.easy.web.DTOUtil
import com.example.win.easy.web.dto.SongListDTO
import com.example.win.easy.web.network.AllSongListNetworkFetchService
import com.example.win.easy.web.network.NetworkFetchService
import com.example.win.easy.web.request.BackendRequestService
import com.example.win.easy.web.service.LoginService
import com.example.win.easy.web.service.SongListWebService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WebSongListModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideSongListWebService(dtoUtil: DTOUtil, allSongListNetworkFetchService: NetworkFetchService<List<SongListDTO>>): SongListWebService = SongListWebService(dtoUtil, allSongListNetworkFetchService)

    @JvmStatic
    @Provides
    @Singleton
    fun provideAllSongListNetworkFetchService(backendRequestService: BackendRequestService, loginService: LoginService): NetworkFetchService<List<SongListDTO>> = AllSongListNetworkFetchService(backendRequestService, loginService)

    @JvmStatic
    @Provides
    @Singleton
    fun provideSongListDto(backendRequestService: BackendRequestService): SongListDto = SongListDto(backendRequestService)
}