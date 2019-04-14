package com.example.win.easy.display.component;

import android.widget.TabHost;

import com.example.win.easy.display.SongList;
import com.example.win.easy.display.interfaces.SongListView;

import java.util.ArrayList;
import java.util.List;

public class SongListViewImpl implements SongListView {

    private static SongListViewImpl instance=new SongListViewImpl();
    public static SongListViewImpl getInstance(){return instance;}
    private SongListViewImpl(){}

    /**
     * 展示一首歌的所有所在歌单
     * @param songLists
     */
    @Override
    public void update(List<SongList> songLists, TabHost tabHost, Integer[] tabs) {

        int songListAmount=songLists.size();
        List<TabHost.TabSpec> tabSpecs= new ArrayList<>();

        tabHost.clearAllTabs();
        tabHost.setup();
        for (int songListIndex=0;songListIndex<songListAmount;songListIndex++){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(String.valueOf(songListIndex));
            tabSpec.setIndicator(songLists.get(songListIndex).getName());
            tabSpec.setContent(tabs[songListIndex]);
            tabHost.addTab(tabSpec);
        }
//        TabHost.TabSpec[] spec=new TabHost.TabSpec[songLists.size()];
//        String[] tabName=new String[]{"tab1","tab2","tab3"};
//
//        for(int i = 0; i<Constants.NumberOfList; i++){
//            spec[i]=tabHost.newTabSpec(tabName[i]);
//            spec[i].setIndicator(songLists.get(i).getName(),null);
//            spec[i].setContent((tabs[i]));
//        }
//        tabHost.clearAllTabs();
//        for(int i=0;i<Constants.NumberOfList;i++)
//            tabHost.addTab(spec[i]);
    }
}
