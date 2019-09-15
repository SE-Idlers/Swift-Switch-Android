package com.example.win.easy.dagger.module;

import com.example.win.easy.web.DTOUtil;
import com.example.win.easy.web.dto.SongListDTO;
import com.example.win.easy.web.network.AllSongListNetworkFetchService;
import com.example.win.easy.web.network.NetworkFetchService;
import com.example.win.easy.web.request.BackendRequestService;
import com.example.win.easy.web.service.LoginService;
import com.example.win.easy.web.service.SongListWebService;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WebSongListModule {

    @Provides
    @Singleton
    static SongListWebService provideSongListWebService(DTOUtil dtoUtil, NetworkFetchService<List<SongListDTO>> allSongListNetworkFetchService){
        return new SongListWebService(dtoUtil,allSongListNetworkFetchService);
    }

    @Provides @Singleton static NetworkFetchService<List<SongListDTO>> provideAllSongListNetworkFetchService(BackendRequestService backendRequestService, LoginService loginService){
        return new AllSongListNetworkFetchService(backendRequestService,loginService);
    }

}
