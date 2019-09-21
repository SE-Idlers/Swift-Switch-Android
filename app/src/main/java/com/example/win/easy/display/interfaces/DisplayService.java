package com.example.win.easy.display.interfaces;

import com.example.win.easy.tool.SongListWithSongs;
import com.example.win.easy.display.DisplayMode;
import com.example.win.easy.repository.db.data_object.SongDO;

public interface DisplayService {

    void next();

    void previous();

    void pause();

    void start();

    boolean isPlaying();

    void setMode(DisplayMode mode);

    boolean setDisplayList(SongListWithSongs songListWithSongs);

    SongListWithSongs getDisplayList();

    void displayByIndex(int index);

    boolean restartWith(SongDO songDO, SongListWithSongs listSongAt);//根据具体实现，我修改了一下参数
}
