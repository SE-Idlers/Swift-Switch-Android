package com.example.win.easy.song;

import com.example.win.easy.feature.InternalInformation;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class Song {

    private String name;

    private String author;

    List<Character> sequence;

    DataSource source;

    InternalInformation internalInformation;

    private File absolutePath;

    public File getAbsolutePath(){
        return absolutePath;
    }
}
