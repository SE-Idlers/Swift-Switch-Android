package com.example.win.easy.dagger.module;

import com.example.win.easy.web.DTOUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DTOModule {

    @Provides
    @Singleton
    static DTOUtil provideDTOUtil(){
        return new DTOUtil();
    }

}

