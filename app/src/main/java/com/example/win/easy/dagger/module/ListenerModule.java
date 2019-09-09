package com.example.win.easy.dagger.module;

import com.example.win.easy.display.interfaces.DisplayService;
import com.example.win.easy.view.activity.interfaces.SongListView;
import com.example.win.easy.dagger.scope.DashboardScope;
import com.example.win.easy.factory.ListenerFactory;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;

import dagger.Module;
import dagger.Provides;

@Module
public class ListenerModule {

    SongListView songListView;

    public ListenerModule(SongListView songListView){
        this.songListView=songListView;
    }

    @Provides @DashboardScope
    ListenerFactory provideListenerFactory(DisplayService displayService){
        return new ListenerFactory(displayService,songListView);
    }

    @Provides @DashboardScope
    RecognitionProxy provideRecognitionProxyWithFourGestures(){
        return new RecognitionProxyWithFourGestures();
    }
}
