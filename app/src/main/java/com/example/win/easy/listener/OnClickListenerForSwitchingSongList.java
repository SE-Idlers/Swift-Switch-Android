package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.repository.db.data_object.SongDO;

import lombok.Builder;

@Builder
public class OnClickListenerForSwitchingSongList implements View.OnClickListener {

    private DisplayManager displayManager;
    private SongDO songDO;

    @Override
    public void onClick(View v) {
        int index=displayManager.getDisplayList().getSongDOs().indexOf(songDO);
        displayManager.displayByIndex(index);
    }
}
