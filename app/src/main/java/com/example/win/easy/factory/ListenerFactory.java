package com.example.win.easy.factory;

import com.example.win.easy.activity.interfaces.SongListView;
import com.example.win.easy.dagger.scope.DashboardScope;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.listener.OnClickListenerForSelectingSong;
import com.example.win.easy.listener.OnClickListenerForSwitchingSongList;
import com.example.win.easy.listener.OnTabSelectedListenerForSelectingSong;
import com.example.win.easy.listener.OnTabSelectedListenerForSwitchingSongList;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.tool.SongList;

import java.util.List;

import javax.inject.Inject;

@DashboardScope
public class ListenerFactory {

    private DisplayManager displayManager;
    private SongListView songListView;

    @Inject
    public ListenerFactory(DisplayManager displayManager,SongListView songListView) {
        this.displayManager = displayManager;
        this.songListView=songListView;
    }

    public OnClickListenerForSelectingSong create(SongPojo songPojo,
                                                  List<SongPojo> allSongs,
                                                  List<SongListPojo> allSongLists,
                                                  List<SongXSongList> allRelation) {
        return OnClickListenerForSelectingSong.builder()
                .songPojo(songPojo)
                .allSongs(allSongs)
                .allSongLists(allSongLists)
                .allRelation(allRelation)
                .displayManager(displayManager)
                .songListView(songListView)
                .build();
    }

    public OnClickListenerForSwitchingSongList create(SongPojo songPojo){
        return OnClickListenerForSwitchingSongList.builder()
                .songPojo(songPojo)
                .displayManager(displayManager)
                .build();
    }

    public OnTabSelectedListenerForSelectingSong create(){
        return OnTabSelectedListenerForSelectingSong.builder()
                .build();
    }

    public OnTabSelectedListenerForSwitchingSongList create(List<SongList> appearanceLists){
        return OnTabSelectedListenerForSwitchingSongList.builder()
                .appearanceLists(appearanceLists)
                .displayManager(displayManager)
                .build();
    }

}