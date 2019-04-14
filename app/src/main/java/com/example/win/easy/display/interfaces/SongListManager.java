package com.example.win.easy.display.interfaces;

import com.example.win.easy.display.SongList;
import com.example.win.easy.song.Song;

import java.util.List;

public interface SongListManager {


    boolean changePriority(SongList songList, int indexTo);

    List<SongList> appearanceListsOf(Song song);

}
