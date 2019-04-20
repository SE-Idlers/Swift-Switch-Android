package com.example.win.easy.listener;

import android.view.View;
import android.widget.AdapterView;

import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.interfaces.SongListView;
import com.example.win.easy.song.Song;

import java.util.List;

public class OnItemClickListenerForSearchingView implements AdapterView.OnItemClickListener {

    private DisplayManager displayManager;
    private SongList songListOfThisTab;
    private SongListView songListView;
    private SongListManager songListManager=SongListMangerImpl.getInstance();

    public OnItemClickListenerForSearchingView(SongList songListOfThisTab,DisplayManager displayManager,SongListView songListView){
        this.songListOfThisTab=songListOfThisTab;
        this.displayManager=displayManager;
        this.songListView=songListView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Song clickedSong=songListOfThisTab.getSongAt((int)id);//获得被点击的歌曲
        List<SongList> appearanceLists=songListManager.appearanceListsOf(clickedSong);
        songListView.update(clickedSong,appearanceLists);
        displayManager.restartWith(clickedSong,appearanceLists.get(0));
    }
}
