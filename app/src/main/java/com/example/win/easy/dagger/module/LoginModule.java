package com.example.win.easy.dagger.module;

import com.example.win.easy.web.request.BackendRequestService;
import com.example.win.easy.web.service.LoginService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {

    @Provides
    @Singleton
    static LoginService providesLoginService(BackendRequestService backendRequestService){
        return new LoginService(backendRequestService);
    }

}
