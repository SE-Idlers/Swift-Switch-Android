package com.example.win.easy.display.interfaces;

import com.example.win.easy.display.DisplayMode;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.song.Song;

public interface DisplayManager {

    //我觉得next和previous应该为公有，而changesong应该为私有，改了一下
    void next();

    void previous();

    void pause();

    void start();

    boolean isPlaying();
    void setMode(DisplayMode mode);

    boolean setDisplayList(SongList songList);

    SongList getDisplayList();

    void displayByIndex(int index);

    boolean restartWith(Song song,SongList listSongAt);//根据具体实现，我修改了一下参数
}
