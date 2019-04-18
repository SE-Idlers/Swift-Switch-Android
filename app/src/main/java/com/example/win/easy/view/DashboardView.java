package com.example.win.easy.view;

import android.widget.ListView;
import android.widget.TabHost;

import com.example.win.easy.Constants;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.R;
import com.example.win.easy.songList.TemporaryListGenerator;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.listener.OnItemClickListenerForSearchingView;
import com.example.win.easy.listener.OnItemClickListenerForSongListView;
import com.example.win.easy.listener.OnTabChangeListenerForSearchingView;
import com.example.win.easy.listener.OnTabChangeListenerForSongListView;
import com.example.win.easy.song.Song;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;

import java.util.List;

public class DashboardView implements SongListView, SearchingView {

    private static DashboardView instance=new DashboardView();
    public static DashboardView getInstance(){return instance;}
    private DashboardView(){}

    private static ListView[] listViews =new ListView[Constants.NumberOfList];
    private static Integer[] listViewID=new Integer[]{R.id.listView1,R.id.listView2,R.id.listView3};
    private static Integer[] tabs=new Integer[]{R.id.tab1,R.id.tab2,R.id.tab3};
    private static TabHost tabHost = MainActivity.mainActivity.findViewById(android.R.id.tabhost);
    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private TemporaryListGenerator tool= TemporaryListGenerator.getInstance();

    static {
        for(int i=0;i<listViews.length;i++) {
            listViews[i] = MainActivity.mainActivity.findViewById(listViewID[i]);
            listViews[i].setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

    }

    public void updateConnection(List<SongList> lists,TabHost.OnTabChangeListener listener) {
        int listAmount=lists.size();//list数量

        tabHost.clearAllTabs();//清除标签
        tabHost.setup();//准备
        tabHost.setOnTabChangedListener(listener);

        for (int songListIndex=0;songListIndex<listAmount;songListIndex++){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(String.valueOf(songListIndex));
            tabSpec.setIndicator(lists.get(songListIndex).getName());//设置上栏的名称
            tabSpec.setContent(tabs[songListIndex]);//设置下栏的内容对应关系
            tabHost.addTab(tabSpec);

        }
    }

    @Override
    public void update(Song song,List<SongList> appearanceLists) {
        //截断，防止太长
        if(appearanceLists.size()>Constants.NumberOfList)
            appearanceLists=appearanceLists.subList(0,Constants.NumberOfList);
        updateConnection(
                appearanceLists,
                new OnTabChangeListenerForSongListView(tabHost, displayManager, appearanceLists)
        );//更新Dashboard连接关系
        updateViewContent(appearanceLists);//更新内容
        updateSongListViewListener(appearanceLists);//更新歌单监听器
    }

    @Override
    public void update(List<Integer> sortedIndices) {
        List<SongList> searchResult=tool.toSearchResult(sortedIndices);//获得按来源得到的结果列表
        updateConnection(
                searchResult,
                new OnTabChangeListenerForSearchingView(tabHost)
        );//更新Dashboard连接关系
        updateViewContent(searchResult);//更新内容
        updateSearchingViewListener(searchResult);//更新搜索结果列表监听器
    }

    private void updateViewContent(List<SongList> lists){
        int amount=lists.size();
        for (int index=0;index<amount;index++){
            listViews[index].setAdapter(SongList.toArrayAdapter(lists.get(index)));
        }
    }
    private void updateSearchingViewListener(List<SongList> searchResult){
        int groupAmount=searchResult.size();//结果组的数量
        for (int groupIndex=0;groupIndex<groupAmount;groupIndex++){
            //设置监听器
            listViews[groupIndex].setOnItemClickListener(
                    new OnItemClickListenerForSearchingView(
                            searchResult.get(groupIndex),
                            displayManager,
                            this
                    )
            );
        }
    }

    private void updateSongListViewListener(List<SongList> appearanceLists){
        int appearanceAmount=appearanceLists.size();
        for (int appearanceIndex=0;appearanceIndex<appearanceAmount;appearanceIndex++){
            //设置监听器
            listViews[appearanceIndex].setOnItemClickListener(
                        new OnItemClickListenerForSongListView(
                                displayManager
                        )
                    );
        }
    }
}
