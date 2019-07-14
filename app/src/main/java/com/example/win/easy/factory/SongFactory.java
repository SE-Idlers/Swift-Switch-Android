package com.example.win.easy.factory;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.parser.interfaces.FilenameParser;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.domain.NetworkSong;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SongFactory {

    FilenameParser<Character> parser;

    @Inject
    public SongFactory(FilenameParser<Character> parser){
        this.parser=parser;
    }

    public SongPojo create(NetworkSong networkSong){
        return SongPojo.builder()
                .name(networkSong.totalName)
                .author(networkSong.author)
                .source(networkSong.source)
                .uid(networkSong.uid)
                .remoteId(networkSong.remoteId)
                .sequence(parser.parse(networkSong.name))
                .build();
    }



    public SongPojo create(File songFile){
        String abPath=songFile.getAbsolutePath();
        return SongPojo.builder()
                .name(abPath.substring(abPath.lastIndexOf('/')+1, abPath.lastIndexOf('.')))
                .author(abPath.substring(abPath.lastIndexOf('/')+1,abPath.indexOf('-')))
                .source(DataSource.Local)
                .sequence(parser.parse(abPath.substring(abPath.indexOf('-')+1,abPath.lastIndexOf('.'))))
                .songPath(abPath)
                .build();
    }
}
