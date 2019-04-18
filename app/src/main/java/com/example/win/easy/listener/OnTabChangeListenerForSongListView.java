package com.example.win.easy.listener;

import android.widget.TabHost;

import com.example.win.easy.songList.SongList;
import com.example.win.easy.display.interfaces.DisplayManager;

import java.util.List;

public class OnTabChangeListenerForSongListView implements TabHost.OnTabChangeListener {

    private TabHost tabHost;
    private DisplayManager displayManager;
    private List<SongList> appearanceLists;

    public OnTabChangeListenerForSongListView(TabHost tabHost, DisplayManager displayManager, List<SongList> appearanceLists){
        this.tabHost=tabHost;
        this.displayManager=displayManager;
        this.appearanceLists=appearanceLists;
    }

    @Override
    public void onTabChanged(String tabId) {
        tabHost.setCurrentTabByTag(tabId);
        int tabIndex=Integer.valueOf(tabId).intValue();
        displayManager.setDisplayList(appearanceLists.get(tabIndex));
    }
}
