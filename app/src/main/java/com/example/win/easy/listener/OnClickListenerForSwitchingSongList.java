package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.display.interfaces.DisplayService;
import com.example.win.easy.repository.db.data_object.SongDO;

import lombok.Builder;

@Builder
public class OnClickListenerForSwitchingSongList implements View.OnClickListener {

    private DisplayService displayService;
    private SongDO songDO;

    @Override
    public void onClick(View v) {
        int index= displayService.getDisplayList().getSongDOs().indexOf(songDO);
        displayService.displayByIndex(index);
    }
}
