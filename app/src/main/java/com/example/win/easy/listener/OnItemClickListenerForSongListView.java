package com.example.win.easy.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.win.easy.display.interfaces.DisplayManager;

public class OnItemClickListenerForSongListView implements AdapterView.OnItemClickListener {

    private DisplayManager displayManager;

    public OnItemClickListenerForSongListView(DisplayManager displayManager){
        this.displayManager=displayManager;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayManager.displayByIndex((int)id);
    }
}
