package com.example.win.easy.dagger.module;

import com.example.win.easy.recognization.component.RecognitionServiceWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RecognitionModule {

    @Provides
    @Singleton
    static RecognitionService provideRecognitionService(){
        return new RecognitionServiceWithFourGestures();
    }

}
