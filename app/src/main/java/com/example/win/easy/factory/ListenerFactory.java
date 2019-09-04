package com.example.win.easy.factory;

import com.example.win.easy.view.activity.interfaces.SongListView;
import com.example.win.easy.dagger.scope.DashboardScope;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.listener.OnClickListenerForSelectingSong;
import com.example.win.easy.listener.OnClickListenerForSwitchingSongList;
import com.example.win.easy.listener.OnTabSelectedListenerForSelectingSong;
import com.example.win.easy.listener.OnTabSelectedListenerForSwitchingSongList;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
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

    public OnClickListenerForSelectingSong create(SongDO songDO,
                                                  List<SongDO> allSongs,
                                                  List<SongListDO> allSongLists,
                                                  List<SongXSongListDO> allRelation) {
        return OnClickListenerForSelectingSong.builder()
                .songDO(songDO)
                .allSongs(allSongs)
                .allSongLists(allSongLists)
                .allRelation(allRelation)
                .displayManager(displayManager)
                .songListView(songListView)
                .build();
    }

    public OnClickListenerForSwitchingSongList create(SongDO songDO){
        return OnClickListenerForSwitchingSongList.builder()
                .songDO(songDO)
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