package com.example.win.easy.dagger.module

import com.example.win.easy.recognization.Image
import com.example.win.easy.recognization.component.RecognitionServiceWithFourGestures
import com.example.win.easy.recognization.interfaces.RecognitionService
import com.example.win.easy.view.ImageService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RecognitionModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideRecognitionService(): RecognitionService = RecognitionServiceWithFourGestures()

    @JvmStatic
    @Provides
    @Singleton
    fun provideImageService(): ImageService = ImageService()
}