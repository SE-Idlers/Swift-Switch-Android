package com.example.win.easy.persistence;

import com.example.win.easy.song.Song;

import java.io.File;
import java.util.Map;

public class FileSongMapConfigurationPersistence implements ConfigurationPersistence<Map<File, Song>>{

    @Override
    public void save(Map<File, Song> entity) {
        
    }

    @Override
    public Map<File, Song> load() {
        return null;
    }
}
