package com.example.win.easy.dagger.component

import com.example.win.easy.dagger.module.*
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.parser.filter.FilterStrategy
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.recognization.interfaces.RecognitionService
import com.example.win.easy.repository.RelationRepository
import com.example.win.easy.repository.SongListRepository
import com.example.win.easy.repository.SongRepository
import com.example.win.easy.view.ImageService
import com.example.win.easy.web.DTOUtil
import com.example.win.easy.web.service.LoginService
import dagger.Component
import javax.inject.Singleton

@Component(
        modules = [
            ThreadModule::class,
            BackendRequestModule::class,
            LoginModule::class,
            UtilModule::class,
            WebSongListModule::class,
            DisplayModule::class,
            RepositoryModule::class,
            ParserModule::class,
            DownloadModule::class,
            RecognitionModule::class])
@Singleton
interface AppComponent {
    fun requireDisplayManager(): DisplayService
    fun requireSongRepository(): SongRepository
    fun requireSongListRepository(): SongListRepository
    fun requireRelationRepository(): RelationRepository
    fun requireDTOUtil(): DTOUtil
    fun requireImageService(): ImageService
    fun requireRecognitionService(): RecognitionService
    fun requireFilterStrategy(): FilterStrategy<List<Char>>
    fun requireLoginService(): LoginService
    fun requireDownloadServiceAdapter(): DownloadServiceAdapter
    fun requireParser(): FilenameParser<Char>
}