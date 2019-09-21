package com.example.win.easy.dagger.module;

import com.example.win.easy.dagger.scope.DashboardScope;
import com.example.win.easy.recognization.component.RecognitionServiceWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionService;

import dagger.Module;
import dagger.Provides;

@Module
public class ListenerModule {

    @Provides @DashboardScope
    RecognitionService provideRecognitionProxyWithFourGestures(){
        return new RecognitionServiceWithFourGestures();
    }
}
