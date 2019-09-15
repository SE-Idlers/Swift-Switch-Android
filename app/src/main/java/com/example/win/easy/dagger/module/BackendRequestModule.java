package com.example.win.easy.dagger.module;

import com.example.win.easy.web.request.BackendRequestService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class BackendRequestModule {

    @Provides @Singleton static BackendRequestService provideBackendResourceWebService(@Named("serverUrl") String serverUrl){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BackendRequestService.class);
    }

    @Provides @Named("serverUrl") static String provideServerUrl(){
        return "http://guohere.com:9000/";
    }
}
