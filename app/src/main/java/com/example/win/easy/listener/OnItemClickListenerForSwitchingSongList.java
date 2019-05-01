package com.example.win.easy.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;

public class OnItemClickListenerForSwitchingSongList implements AdapterView.OnItemClickListener {

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayManager.displayByIndex(position);
    }
}
