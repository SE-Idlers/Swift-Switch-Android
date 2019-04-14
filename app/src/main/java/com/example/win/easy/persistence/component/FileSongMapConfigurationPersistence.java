package com.example.win.easy.persistence.component;

import com.example.win.easy.persistence.interfaces.AbstractJsonifyConfigurationPersistence;
import com.example.win.easy.song.Song;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//阿里巴巴的fastjson框架


public class FileSongMapConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<Map<File,Song>> {

    private static String fileDir="/SwiftSwitch/src/MapOfFileAndSong.json";
    private static FileSongMapConfigurationPersistence instance=new FileSongMapConfigurationPersistence();
    public static FileSongMapConfigurationPersistence getInstance(){return  instance;}
    private FileSongMapConfigurationPersistence(){
        super(fileDir);
    }

    @Override
    protected Class<Map<File, Song>> getClassInformation() {
        Map<File, Song> tempMap=new HashMap<>();
        return (Class<Map<File, Song>>) tempMap.getClass();
    }
}
