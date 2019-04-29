package com.example.win.easy.listener;

import android.support.design.widget.TabLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.view.DashboardView;
import com.example.win.easy.view.interfaces.SearchingView;

import java.util.List;

public class OnTabSelectedListenerForSelectingSong implements TabLayout.OnTabSelectedListener {

    private SearchingView searchingView= DashboardView.getInstance();
    private ListView listView= MainActivity.mainActivity.findViewById(R.id.listView);
    private List<SongList> candidates;
    private List<ArrayAdapter<String>> contents;
    public OnTabSelectedListenerForSelectingSong(List<SongList> candidates, List<ArrayAdapter<String>> contents){
        this.candidates=candidates;
        this.contents=contents;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        searchingView.setPrincipal(candidates.get(tab.getPosition()));
        listView.setAdapter(contents.get(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
