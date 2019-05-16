package com.example.win.easy.listener;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.songList.SongList;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.List;

public class OnTabSelectedListenerForSwitchingSongList implements QMUITabSegment.OnTabSelectedListener {

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private List<SongList> appearanceLists;

    public OnTabSelectedListenerForSwitchingSongList(List<SongList> appearanceLists){
        this.appearanceLists=appearanceLists;
    }

    @Override
    public void onTabSelected(int index) {
        displayManager.setDisplayList(appearanceLists.get(index));
    }

    @Override
    public void onTabUnselected(int index) {

    }

    @Override
    public void onTabReselected(int index) {

    }

    @Override
    public void onDoubleTap(int index) {

    }
}
