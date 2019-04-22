package com.example.win.easy.song;

/**
 * 歌曲的各种信息及其set和get函数
 */

import com.example.win.easy.feature.InternalInformation;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class Song {

    private String name;

    private String author;

    private String abpath;

    private List<Character> sequence;

    DataSource source;

    InternalInformation internalInformation;

    private File absolutePath;

    public Song(){};

    public Song(String sname){
        name=sname;
    }

    public Song(String path,String sname){
        abpath=path;
        name=sname;
    }

    public String getName(){return name;}

    public File getAbsolutePath(){
        return absolutePath;
    }
}
