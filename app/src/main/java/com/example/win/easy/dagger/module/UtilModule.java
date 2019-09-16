package com.example.win.easy.dagger.module;

import com.example.win.easy.parser.interfaces.FilenameParser;
import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.web.DTOUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Provides
    @Singleton
    static DTOUtil provideDTOUtil(FilenameParser<Character> filenameParser){
        return new DTOUtil(filenameParser);
    }

    @Provides
    @Singleton
    static VOUtil provideVOUtil(){
        return new VOUtil();
    }
}

