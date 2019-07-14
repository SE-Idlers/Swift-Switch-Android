package com.example.win.easy.tool;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SongList {
    private SongListPojo songListPojo;

    private List<SongPojo> songPojos;

    public String getName(){return songListPojo.name;}
    public DataSource getSource(){return songListPojo.source;}
    public List<SongPojo> getSongPojos(){return songPojos;}
}
