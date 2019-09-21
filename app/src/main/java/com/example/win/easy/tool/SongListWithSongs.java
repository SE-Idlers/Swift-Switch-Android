package com.example.win.easy.tool;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SongListWithSongs {
    private SongListDO songListDO;

    private SongListVO songList;
    private List<SongVO> songs;

    private List<SongDO> songDOs;

    public String getName(){return songListDO.getName();}
    public DataSource getSource(){return songListDO.getSource();}
    public List<SongDO> getSongDOs(){return songDOs;}
}
