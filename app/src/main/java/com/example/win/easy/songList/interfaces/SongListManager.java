package com.example.win.easy.songList.interfaces;

import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongList;

import java.util.List;

public interface SongListManager {


    boolean changePriority(SongList songList, int indexTo);

    List<SongList> appearanceListsOf(Song song);

}
