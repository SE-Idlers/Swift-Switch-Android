package com.example.win.easy.view.interfaces;

import com.example.win.easy.songList.SongList;
import com.example.win.easy.song.Song;

import java.util.List;

public interface SongListView {

    void update(Song song,List<SongList> appearanceLists);

}
