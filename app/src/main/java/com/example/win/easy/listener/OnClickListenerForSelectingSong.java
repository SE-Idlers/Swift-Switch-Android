package com.example.win.easy.listener;

import android.view.View;

import com.example.win.easy.ActivityHolder;
import com.example.win.easy.DataSource;
import com.example.win.easy.tool.SongList;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.activity.interfaces.SongListView;

import java.util.ArrayList;
import java.util.List;

public class OnClickListenerForSelectingSong implements View.OnClickListener {

    private SongListView songListView= ActivityHolder.getLockActivity().get();
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    private SongPojo songPojo;
    private List<SongPojo> allSongs;
    private List<SongListPojo> allSongLists;
    private List<SongXSongList> allRelation;
    public OnClickListenerForSelectingSong(SongPojo songPojo,List<SongPojo> allSongs,List<SongListPojo> allSongLists, List<SongXSongList> allRelation){
        this.songPojo=songPojo;
        this.allSongs=allSongs;
        this.allSongLists=allSongLists;
        this.allRelation=allRelation;
    }

    @Override
    public void onClick(View v) {
        List<SongList> appearanceLists=appearanceListsOf(songPojo);
        songListView.updateToSwitchingSongList(appearanceLists);
        displayManager.restartWith(songPojo,appearanceLists.get(0));
    }

    private List<SongList> appearanceListsOf(SongPojo songPojo){
        List<SongList> appearanceLists=new ArrayList<>();
        //TODO 统计出现的歌单
        //将本地音乐作为一个歌单加入
        appearanceLists.add(new SongList(new SongListPojo(DataSource.Local),allSongs));
        return appearanceLists;
    }
}
