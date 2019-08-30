package com.example.win.easy.factory;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.parser.interfaces.FilenameParser;
import com.example.win.easy.repository.db.data_object.SongDO;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class __SongFactory {

    FilenameParser<Character> parser;

    @Inject
    public __SongFactory(FilenameParser<Character> parser){
        this.parser=parser;
    }

    public SongDO create(File songFile){
        String abPath=songFile.getAbsolutePath();
        return SongDO.builder()
                .name(abPath.substring(abPath.lastIndexOf('/')+1, abPath.lastIndexOf('.')))
                .author(abPath.substring(abPath.lastIndexOf('/')+1,abPath.indexOf('-')))
                .source(DataSource.Local)
                .sequence(parser.parse(abPath.substring(abPath.indexOf('-')+1,abPath.lastIndexOf('.'))))
                .songPath(abPath)
                .build();
    }
}
