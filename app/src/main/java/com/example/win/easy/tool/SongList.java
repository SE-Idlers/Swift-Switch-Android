package com.example.win.easy.tool;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SongList {
    private SongListDO songListDO;

    private List<SongDO> songDOs;

    public String getName(){return songListDO.getName();}
    public DataSource getSource(){return songListDO.getSource();}
    public List<SongDO> getSongDOs(){return songDOs;}
}
