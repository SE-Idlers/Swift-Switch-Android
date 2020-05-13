package com.example.win.easy.dagger.module

import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.repository.SongDto
import com.example.win.easy.repository.SongListDto
import com.example.win.easy.web.dto.SongListDTO
import com.example.win.easy.web.request.BackendRequestService
import com.example.win.easy.web.service.LoginService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object WebSongListModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideSongListDto(backendRequestService: BackendRequestService): SongListDto = SongListDto(backendRequestService)

    @JvmStatic
    @Provides
    @Singleton
    fun provideSongDto(backendRequestService: BackendRequestService,parser: FilenameParser<Char>): SongDto = SongDto(backendRequestService,parser)
}