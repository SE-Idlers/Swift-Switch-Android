package com.example.win.easy.dagger.module;

import android.media.MediaPlayer;

import com.example.win.easy.display.DisplayServiceImpl;
import com.example.win.easy.display.interfaces.DisplayService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DisplayModule {

    @Provides @Singleton static MediaPlayer mediaPlayer(){
        return new MediaPlayer();
    }
    @Provides @Singleton static DisplayService displayManager(MediaPlayer mediaPlayer){return new DisplayServiceImpl(mediaPlayer); }
}
