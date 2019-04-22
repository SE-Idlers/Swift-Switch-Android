package com.example.win.easy.view;

import android.app.Activity;
import android.app.TabActivity;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.win.easy.Constants;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.R;
import com.example.win.easy.songList.SongListMangerImpl;
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

import java.util.ArrayList;
import java.util.List;

public class DashboardView extends Activity
        implements SongListView, SearchingView {

    private static ListView listView= (ListView) MainActivity.mainActivity.findViewById(R.id.listView);

    TabLayout mytab = (TabLayout) MainActivity.mainActivity.findViewById(R.id.tab);

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private TemporaryListGenerator tool= TemporaryListGenerator.getInstance();
    private SongListMangerImpl songListManger=SongListMangerImpl.getInstance();

    private static DashboardView instance=new DashboardView();
    public static DashboardView getInstance(){return instance;}

    private DashboardView(){
        mytab.addTab(mytab.newTab().setText("默认歌单"));
        mytab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               addsong_update(SongListMangerImpl.getInstance().getSongListAtIndex(mytab.getSelectedTabPosition()));
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String NameOfItemClicked=listView.getAdapter().getItem(position).toString();
                Song song=new Song(NameOfItemClicked);
                List<SongList> list=SongListMangerImpl.getInstance().appearanceListsOf(song);
                updateViewContent(list);
            }
        });
    }

    public void AddTab(String string){
        mytab.addTab(mytab.newTab().setText(string));
    }

    public TabLayout  getMytab(){
        return  mytab;
    }

    public void addsong_update(SongList songList){
        listView.setAdapter(songList.toArrayAdapter());
    }

    private void updateViewContent(List<SongList> lists){
        int amount=lists.size();
        int curr_number_of_tab=mytab.getTabCount();
        if(amount<=curr_number_of_tab) {//匹配到的歌单少于等于当前Tab的个数
            for (int index = 0; index < amount; index++) {
                mytab.getTabAt(index).setText(lists.get(index).getName());
            }
            for(int index=curr_number_of_tab-1;index>=amount;index--)
                mytab.removeTabAt(index);
        }
        else if(amount>Constants.NumberOfList){//匹配到的歌单大于三个
                for (int index=0;index<Constants.NumberOfList;index++){
                    mytab.getTabAt(index).setText(lists.get(index).getName());
                }
            }
            else {
            for (int index = 0; index < curr_number_of_tab; index++) {
                mytab.getTabAt(index).setText(lists.get(index).getName());
            }
            for(int index=0;index<Constants.NumberOfList;index++){
              AddTab(lists.get(index).getName());
            }
        }
        listView.setAdapter(lists.get(0).toArrayAdapter());
    }


    //以下是我没用到的函数 ，防止报错，我都写成了注释

    public void updateConnection(List<SongList> lists,TabHost.OnTabChangeListener listener) {
       /* int listAmount=lists.size();//list数量

        tabHost.clearAllTabs();//清除标签
        tabHost.setup();//准备
        tabHost.setOnTabChangedListener(listener);

        for (int songListIndex=0;songListIndex<listAmount;songListIndex++){
            TabHost.TabSpec tabSpec=tabHost.newTabSpec(String.valueOf(songListIndex));
            tabSpec.setIndicator(lists.get(songListIndex).getName());//设置上栏的名称
            tabSpec.setContent(tabs[songListIndex]);//设置下栏的内容对应关系
            tabHost.addTab(tabSpec);
        }*/
    }

    @Override
    public void update(Song song,List<SongList> appearanceLists) {
        //截断，防止太长
       /* if(appearanceLists.size()>Constants.NumberOfList)
            appearanceLists=appearanceLists.subList(0,Constants.NumberOfList);
        updateConnection(
                appearanceLists,
                new OnTabChangeListenerForSongListView(tabHost, displayManager, appearanceLists)
        );//更新Dashboard连接关系
        updateViewContent(appearanceLists);//更新内容
        updateSongListViewListener(appearanceLists);//更新歌单监听器*/
    }

    @Override
    public void update(List<Integer> sortedIndices) {
       /* List<SongList> searchResult=tool.toSearchResult(sortedIndices);//获得按来源得到的结果列表
        updateConnection(
                searchResult,
                new OnTabChangeListenerForSearchingView(tabHost)
        );//更新Dashboard连接关系
        updateViewContent(searchResult);//更新内容
        updateSearchingViewListener(searchResult);//更新搜索结果列表监听器*/
    }

    private void updateSearchingViewListener(List<SongList> searchResult){
       /* int groupAmount=searchResult.size();//结果组的数量
        for (int groupIndex=0;groupIndex<groupAmount;groupIndex++){
            //设置监听器
            listViews[groupIndex].setOnItemClickListener(
                    new OnItemClickListenerForSearchingView(
                            searchResult.get(groupIndex),
                            displayManager,
                            this
                    )
            );
        }*/
    }
    private void updateSongListViewListener(List<SongList> appearanceLists){
        /*int appearanceAmount=appearanceLists.size();
        for (int appearanceIndex=0;appearanceIndex<appearanceAmount;appearanceIndex++){
            //设置监听器
            listViews[appearanceIndex].setOnItemClickListener(
                        new OnItemClickListenerForSongListView(
                                displayManager
                        )
                    );
        }*/
    }
}
