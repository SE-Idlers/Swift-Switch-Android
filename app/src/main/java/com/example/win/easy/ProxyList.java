package com.example.win.easy;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.win.easy.display.component.DisplayManagerImpl;
import com.example.win.easy.display.component.SongListMangerImpl;
import com.example.win.easy.display.component.SongListViewImpl;
import com.example.win.easy.display.SongList;
import com.example.win.easy.search.SearchResultView;
import com.example.win.easy.song.DataSource;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.SongManager;
import com.example.win.easy.song.SongManagerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyList implements SearchResultView {

    private DisplayManagerImpl displayManagerImpl =DisplayManagerImpl.getInstance();
    private SongListViewImpl songListViewImpl =SongListViewImpl.getInstance();
    private SongListMangerImpl songListMangerImpl =SongListMangerImpl.getInstance();
    private SongManager songManager= SongManagerImpl.getInstance();
    private ProxyMediaPlayer proxyMediaPlayer=ProxyMediaPlayer.getInstance();

    public static ProxyList getInstance(){
        return instance;
    }
    private static ProxyList instance =new ProxyList();
    private ProxyList(){init();}

    private List<SongList> appearanceLists;
    private ListView[] listViews =new ListView[Constants.NumberOfList];
    private Integer[] listViewID=new Integer[]{R.id.listView1,R.id.listView2,R.id.listView3};
    private Integer[] tabs=new Integer[]{R.id.tab1,R.id.tab2,R.id.tab3};
    private TabHost tabHost =MainActivity.mainActivity.findViewById(android.R.id.tabhost);

    @Override
    public void update(List<Integer> sortedIndices) {
        Map<DataSource,List<Song>> map=new HashMap<>();
        List<Song> songs=songManager.selectSongsByIndices(sortedIndices);
        for (Song song:songs){
            if(!map.containsKey(song.getSource())){
                List<Song> candidates=new ArrayList<>();
                candidates.add(song);
                map.put(song.getSource(),candidates);
            }else
                map.get(song.getSource()).add(song);
        }
        List<DataSource> dataSources=new ArrayList<>(map.keySet());
        if(dataSources.size()>Constants.NumberOfList)
            dataSources=dataSources.subList(0,Constants.NumberOfList);
        List<SongList> songLists=new ArrayList<>();
        int dataSourceAmount=dataSources.size();
        for (int dataSourceIndex=0;dataSourceIndex<dataSourceAmount;dataSourceIndex++){
            SongList candidate=SongList.builder()
                    .name(dataSources.get(dataSourceIndex).toString())
                    .songList(map.get(dataSources.get(dataSourceIndex)))
                    .build();
            songLists.add(candidate);
            listViews[dataSourceIndex].setAdapter(SongList.toArrayAdapter(candidate));
            listViews[dataSourceIndex].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    restartWithSong(displayManagerImpl.getDisplayList().getSongAt(position));
                    displayManagerImpl.restartWith(displayManagerImpl.getDisplayList().getSongAt(position),proxyMediaPlayer.getmediaPlayer());
                }
            });
        }
        songListViewImpl.update(songLists,tabHost,tabs);
    }

    public void restartWithSong(Song song){
        appearanceLists=songListMangerImpl.appearanceListsOf(song);
        if(appearanceLists.size()>Constants.NumberOfList){
            appearanceLists=appearanceLists.subList(0,Constants.NumberOfList);
        }
        int appearanceListAmount=appearanceLists.size();
        for (int appearanceListIndex=0;appearanceListIndex<appearanceListAmount;appearanceListIndex++){
            listViews[appearanceListIndex].setAdapter(SongList.toArrayAdapter(appearanceLists.get(appearanceListIndex)));
            listViews[appearanceListIndex].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayManagerImpl.restartWith(displayManagerImpl.getDisplayList().getSongAt(position),proxyMediaPlayer.getmediaPlayer());
                }
            });
        }
        songListViewImpl.update(appearanceLists,tabHost,tabs);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int tabIndex=Integer.valueOf(tabId).intValue();
                listViews[tabIndex].setSelection(
                        appearanceLists.get(tabIndex).getSongList().indexOf(
                                displayManagerImpl.getDisplayList().getSongAt(
                                        proxyMediaPlayer.getCurrentPosition()
                                )
                        )
                );
            }
        });
    }

    public void init(){
        for(int i=0;i<listViews.length;i++) {
            listViews[i] = MainActivity.mainActivity.findViewById(listViewID[i]);
            listViews[i].setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

}
