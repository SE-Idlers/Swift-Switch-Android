package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.activity.interfaces.SongListView;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.tool.SongList;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;

@Builder
public class OnClickListenerForSelectingSong implements View.OnClickListener {

    private SongListView songListView;
    private DisplayManager displayManager;

    private SongDO songDO;
    private List<SongDO> allSongs;
    private List<SongListDO> allSongLists;
    private List<SongXSongListDO> allRelation;

    @Override
    public void onClick(View v) {
        List<SongList> appearanceLists=appearanceListsOf(songDO);
        songListView.updateToSwitchingSongList(appearanceLists);
        displayManager.restartWith(songDO,appearanceLists.get(0));
    }

    private List<SongList> appearanceListsOf(SongDO songDO){
        List<SongList> appearanceLists=new ArrayList<>();
        //TODO 统计出现的歌单
        //将本地音乐作为一个歌单加入
        appearanceLists.add(new SongList(SongListDO.builder().source(DataSource.Local).build(),allSongs));
        return appearanceLists;
    }
}
