package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.song.Song;

public class OnClickListenerForSwitchingSongList implements View.OnClickListener {

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private Song song;

    public OnClickListenerForSwitchingSongList(Song song){
        this.song=song;
    }
    @Override
    public void onClick(View v) {
        int index=displayManager.getDisplayList().getSongList().indexOf(song);
        displayManager.displayByIndex(index);
    }
}
