package com.example.win.easy.display;

import com.example.win.easy.song.Song;

import java.util.List;

public interface SongListView {
    void update(List<SongList> songLists,Song songToDisplay);
}
