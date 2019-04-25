package com.example.win.easy.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.DashboardView;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;

import java.util.List;

public class OnItemClickListenerForSelectingSong implements AdapterView.OnItemClickListener {

    private SongListManager songListManager=SongListMangerImpl.getInstance();
    private SongListView songListView=DashboardView.getInstance();
    private SearchingView searchingView=DashboardView.getInstance();
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song songToDisplay=searchingView.getPrincipal().getSongAt(position);
        List<SongList> appearanceLists= songListManager.appearanceListsOf(songToDisplay);
        songListView.update(songToDisplay,appearanceLists);
        displayManager.restartWith(songToDisplay,appearanceLists.get(0));
    }
}
