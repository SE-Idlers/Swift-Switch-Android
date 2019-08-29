package com.example.win.easy.dagger.module;

import com.example.win.easy.repository.web.request.BackendRequestService;
import com.example.win.easy.repository.web.service.LoginService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {


    @Provides @Singleton static BackendRequestService backendResourceWebService(@Named("serverUrl") String serverUrl){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BackendRequestService.class);
    }

    @Provides @Named("serverUrl") static String serverUrl(){
        return "http://guohere.com:9000/";
    }

    @Provides @Singleton static LoginService providesLoginService(BackendRequestService backendRequestService){
        return new LoginService(backendRequestService);
    }
}
