package com.example.win.easy.display.interfaces;

import android.widget.TabHost;

import com.example.win.easy.display.SongList;

import java.util.List;

public interface SongListView {
    void update(List<SongList> songLists, TabHost tabHost, Integer[] tabs);

}
