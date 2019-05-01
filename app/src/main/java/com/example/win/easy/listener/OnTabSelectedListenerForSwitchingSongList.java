package com.example.win.easy.listener;

import android.support.design.widget.TabLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.songList.SongList;

import java.util.List;

public class OnTabSelectedListenerForSwitchingSongList implements TabLayout.OnTabSelectedListener {

    private DisplayManager displayManager= DisplayManagerImpl.getInstance();
    private ListView listView= MainActivity.mainActivity.findViewById(R.id.listView);
    private List<SongList> appearanceLists;
    private List<ArrayAdapter<String>> contents;

    public OnTabSelectedListenerForSwitchingSongList(List<SongList> appearanceLists,List<ArrayAdapter<String>> contents){
        this.appearanceLists=appearanceLists;
        this.contents=contents;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position=tab.getPosition();
        displayManager.setDisplayList(appearanceLists.get(position));
        listView.setAdapter(contents.get(position));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
