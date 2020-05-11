package com.example.win.easy.dagger.module

import android.media.MediaPlayer
import com.example.win.easy.display.DisplayServiceImpl
import com.example.win.easy.display.interfaces.DisplayService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DisplayModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideMediaPlayer() = MediaPlayer()

    @JvmStatic
    @Provides
    @Singleton
    fun provideDisplayService(mediaPlayer: MediaPlayer): DisplayService = DisplayServiceImpl(mediaPlayer)
}