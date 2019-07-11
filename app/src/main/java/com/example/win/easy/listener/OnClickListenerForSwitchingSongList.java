package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.repository.db.pojo.SongPojo;

public class OnClickListenerForSwitchingSongList implements View.OnClickListener {

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private SongPojo songPojo;

    public OnClickListenerForSwitchingSongList(SongPojo songPojo){
        this.songPojo=songPojo;
    }
    @Override
    public void onClick(View v) {
        int index=displayManager.getDisplayList().getSongPojos().indexOf(songPojo);
        displayManager.displayByIndex(index);
    }
}
