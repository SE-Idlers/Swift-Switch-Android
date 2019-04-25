package com.example.win.easy.view;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.listener.OnItemClickListenerForSelectingSong;
import com.example.win.easy.listener.OnItemClickListenerForSwitchingSongList;
import com.example.win.easy.listener.OnTabSelectedListenerForSelectingSong;
import com.example.win.easy.listener.OnTabSelectedListenerForSwitchingSongList;
import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.TemporaryListGenerator;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;

import java.util.ArrayList;
import java.util.List;

public class DashboardView extends Activity
        implements SongListView, SearchingView {

    private TemporaryListGenerator tool= TemporaryListGenerator.getInstance();
    private TabLayout tabLayout = MainActivity.mainActivity.findViewById(R.id.tab);
    private SongList principal;
    private List<ArrayAdapter<String>> contents=new ArrayList<>();

    private static DashboardView instance=new DashboardView();
    public static DashboardView getInstance(){return instance;}
    private DashboardView(){ }

    @Override
    public void update(Song song,List<SongList> appearanceLists) {
        updateDashboardView(appearanceLists);
        updateTabListener(new OnTabSelectedListenerForSwitchingSongList(appearanceLists,contents));
        updateItemListener(new OnItemClickListenerForSwitchingSongList());
    }

    @Override
    public void update(List<Integer> sortedIndices) {
        List<SongList> candidates = tool.toSearchResult(sortedIndices);//获得按来源得到的结果列表
        updateDashboardView(candidates);//更新视图
        updateTabListener(new OnTabSelectedListenerForSelectingSong(candidates,contents));//更新Tab监听
        updateItemListener(new OnItemClickListenerForSelectingSong());//更新Item监听
    }

    @Override
    public SongList getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(SongList principal) {
        this.principal=principal;
    }

    private void updateDashboardView(List<SongList> newContents) {
        contents.clear();
        tabLayout.removeAllTabs();
        for (SongList songList : newContents) {
            tabLayout.addTab(new TabLayout.Tab().setText(songList.getName()));
            contents.add(songList.toArrayAdapter());
        }
    }

    private void updateTabListener(TabLayout.BaseOnTabSelectedListener listener){
        tabLayout.setOnTabSelectedListener(listener);
    }

    private void updateItemListener(AdapterView.OnItemClickListener listener){
        ListView listView= MainActivity.mainActivity.findViewById(R.id.listView);
        listView.setOnItemClickListener(listener);
    }
}
