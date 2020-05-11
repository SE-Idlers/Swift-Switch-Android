package com.example.win.easy.dagger.module

import android.os.Environment
import com.example.win.easy.download.DownloadService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.download.FileService
import com.example.win.easy.repository.SongRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Module
object DownloadModule {

    @JvmStatic
    @Provides
    @Named("downloadRootDir")
    fun provideDownloadRootDir() = Environment.getExternalStorageDirectory().absolutePath + "/Swift Switch"

    @JvmStatic
    @Provides
    @Named("suffixOfTempFile")
    fun provideSuffixOfTempFile() = "~TEMP"

    @JvmStatic
    @Provides
    @Singleton
    fun provideFileService() = FileService()

    @JvmStatic
    @Provides
    @Singleton
    fun provideDownloadService(fileService: FileService, songRepository: SongRepository) = DownloadService(fileService, songRepository)

    @JvmStatic
    @Provides
    @Singleton
    fun provideDownloadServiceAdapter(downloadDispatcher: ExecutorCoroutineDispatcher, downloadService: DownloadService) = DownloadServiceAdapter(downloadDispatcher,downloadService)
}