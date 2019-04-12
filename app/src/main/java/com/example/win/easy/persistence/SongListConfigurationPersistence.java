package com.example.win.easy.persistence;

import com.example.win.easy.display.SongList;

import java.util.List;

public class SongListConfigurationPersistence implements ConfigurationPersistence<List<SongList>> {
    @Override
    public void save(List<SongList> entity) {

    }

    @Override
    public List<SongList> load() {
        return null;
    }
}
