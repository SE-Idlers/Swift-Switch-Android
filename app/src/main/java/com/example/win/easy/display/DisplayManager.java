package com.example.win.easy.display;

import com.example.win.easy.song.Song;

public interface DisplayManager {

    void changeSong(int songIndex);

    void setMode(DisplayMode mode);

    void setDisplayList(SongList list);

    void restartWith(Song song);
}
