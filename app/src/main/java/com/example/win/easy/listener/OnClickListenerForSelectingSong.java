package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.activity.LockActivity;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.interfaces.SongListView;

public class OnClickListenerForSelectingSong implements View.OnClickListener {

    private SongListManager songListManager=SongListMangerImpl.getInstance();
    private SongListView songListView=LockActivity.lockActivity;
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();
    private LockActivity lockActivity=LockActivity.lockActivity;

    private Song song;
    public OnClickListenerForSelectingSong(Song song){
        this.song=song;
    }

    @Override
    public void onClick(View v) {
        songListView.update(song);
        displayManager.restartWith(song,songListManager.appearanceListsOf(song).get(0));
        lockActivity.updatePauseView();
    }
}
