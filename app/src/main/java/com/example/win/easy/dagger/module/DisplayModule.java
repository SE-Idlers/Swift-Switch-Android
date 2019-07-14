package com.example.win.easy.dagger.module;

import android.media.MediaPlayer;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DisplayModule {

    @Provides @Singleton static MediaPlayer mediaPlayer(){
        return new MediaPlayer();
    }
    @Provides @Singleton static DisplayManager displayManager(MediaPlayer mediaPlayer){return new DisplayManagerImpl(mediaPlayer); }
}
