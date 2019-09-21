package com.example.win.easy.view.lock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.example.win.easy.R;
import com.example.win.easy.tool.SongListWithSongs;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.OnClickFunc;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwitchTab extends QMUILinearLayout {
    @BindView(R.id.tab_segment) QMUITabSegment tabSegment;
    @BindView(R.id.view_pager) ViewPager viewPager;
    private PageGroup pageGroup =new PageGroup();
    private List<QMUITabSegment.OnTabSelectedListener> listeners=new ArrayList<>();
    private List<SongListWithSongs> currentAllSongListWithSongs;

    public SwitchTab(Context context) {
        super(context);

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View thisView=inflater.inflate(R.layout.fragment_search,this);

        ButterKnife.bind(this,thisView);

        tabSegment.setupWithViewPager(viewPager);
        viewPager.setAdapter(pageGroup);
    }

    public void setPagesWithTab(List<SongListWithSongs> allSongListWithSongs, OnClickFunc<SongListWithSongs> tabOnClickFunc, OnClickFunc<SongVO> songOnClickFunc){

        clear();
        record(allSongListWithSongs);

        for(SongListWithSongs songListWithSong: allSongListWithSongs){
            addTab(songListWithSong.getSongList(),tabOnClickFunc);
            addPage(songListWithSong.getSongs(),songOnClickFunc);
        }

        flush();
    }

    private void record(List<SongListWithSongs> allSongListWithSongs) {
        currentAllSongListWithSongs=allSongListWithSongs;
    }

    private void clear(){
        clearListeners();
        tabSegment.reset();
        pageGroup.clear();
    }

    private void clearListeners(){
        for(QMUITabSegment.OnTabSelectedListener listener: listeners)
            tabSegment.removeOnTabSelectedListener(listener);
        listeners.clear();
    }

    private void addTab(SongListVO songList,OnClickFunc<SongListWithSongs> tabOnClickFunc){
        tabSegment.addTab(new QMUITabSegment.Tab(songList.getName()));
        QMUITabSegment.OnTabSelectedListener tabListener=createListener(tabOnClickFunc);
        tabSegment.addOnTabSelectedListener(tabListener);
        record(tabListener);
    }

    private QMUITabSegment.OnTabSelectedListener createListener(OnClickFunc<SongListWithSongs> tabOnClickFunc){
        return new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                tabOnClickFunc.onclick(currentAllSongListWithSongs.get(index),null);
            }
            @Override public void onTabUnselected(int index) {

            }
            @Override public void onTabReselected(int index) {

            }
            @Override public void onDoubleTap(int index) {

            }
        };
    }

    private void record(QMUITabSegment.OnTabSelectedListener tabListener) {
        listeners.add(tabListener);
    }

    private void addPage(List<SongVO> songsInList,OnClickFunc<SongVO> songOnClickFunc){
        pageGroup.addPage(songsInList,songOnClickFunc);
    }

    private void flush(){
        pageGroup.flush();
        tabSegment.notifyDataChanged();
    }
}
