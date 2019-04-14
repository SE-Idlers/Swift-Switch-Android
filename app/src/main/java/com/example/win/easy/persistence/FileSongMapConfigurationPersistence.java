package com.example.win.easy.persistence;

import com.example.win.easy.song.Song;

import java.io.File;
import java.util.Map;

//阿里巴巴的fastjson框架


public class FileSongMapConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<Map<File,Song>>{

    private static String fileDir="/SwiftSwitch/src/MapOfFileAndSong.json";

    public FileSongMapConfigurationPersistence(){
        super(fileDir);
    }
}
