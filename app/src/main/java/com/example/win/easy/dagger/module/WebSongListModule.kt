package com.example.win.easy.dagger.module

import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.dto.SongDto
import com.example.win.easy.dto.SongListDto
import com.example.win.easy.network.BackendRequestService
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
    fun provideSongDto(backendRequestService: BackendRequestService, parser: FilenameParser<Char>): SongDto = SongDto(backendRequestService, parser)
}