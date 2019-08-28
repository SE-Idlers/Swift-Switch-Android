package com.example.win.easy.dagger.module;

import com.example.win.easy.repository.web.request.BackendResourceWebService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {


    @Provides @Singleton static BackendResourceWebService backendResourceWebService(@Named("serverUrl") String serverUrl){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(BackendResourceWebService.class);
    }

    @Provides @Named("serverUrl") static String serverUrl(){
        return "http://guohere.com:9000/";
    }
}
