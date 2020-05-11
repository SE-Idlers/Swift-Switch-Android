package com.example.win.easy.dagger.component

import androidx.lifecycle.ViewModelProvider
import com.example.win.easy.dagger.module.ViewModelModule
import com.example.win.easy.dagger.scope.ViewModelScope
import com.example.win.easy.display.interfaces.DisplayService
import com.example.win.easy.download.DownloadServiceAdapter
import com.example.win.easy.parser.filter.FilterStrategy
import com.example.win.easy.parser.interfaces.FilenameParser
import com.example.win.easy.recognization.interfaces.RecognitionService
import com.example.win.easy.view.ImageService
import com.example.win.easy.web.service.LoginService
import dagger.Component

@Component(dependencies = [AppComponent::class], modules = [ViewModelModule::class])
@ViewModelScope
interface ViewModelComponent {
    fun requireViewModelFactory(): ViewModelProvider.Factory
    fun requireDisplayManager(): DisplayService
    fun requireFilterStrategy(): FilterStrategy<List<Char>>
    fun requireRecognitionService(): RecognitionService
    fun requireLoginService(): LoginService
    fun requireImageService(): ImageService
    fun requireDownloadServiceAdapter(): DownloadServiceAdapter
    fun requireParser(): FilenameParser<Char>
}