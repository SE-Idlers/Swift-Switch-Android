package com.example.win.easy.display;

import android.media.MediaPlayer;
import android.widget.ArrayAdapter;

import com.example.win.easy.MainActivity;
import com.example.win.easy.song.Song;

import java.io.IOException;

public class ImplementDisplayManager implements DisplayManager {

    private SongList displayList;
    private DisplayMode displayMode;
    private Song currentSong;

    private void changeSong(int songIndex ,MediaPlayer mediaPlayer) {
        if (songIndex < 0) {
            //比第一首在前一首
            songIndex =displayList.getSongList().size()-1;
        } else if (songIndex> displayList.getSongList().size()-1) {
            //最后一首的下一首
            songIndex = 0;
        }
        restartWith(displayList.getSongAt(songIndex).getAbsolutePath().toString(),mediaPlayer);
    }

    //这三个函数我先占个位，没想好怎么去实现
    private void random(){}
    private void sample(){}
    private void automata(){}

    public SongList getDisplayList(){return displayList;}

    @Override
    public void next(int currentSong,MediaPlayer mediaPlayer){
        changeSong(++currentSong,mediaPlayer);
    }
    @Override
    public void previous(int currentSong,MediaPlayer mediaPlayer){
        changeSong(--currentSong,mediaPlayer);
    }

    @Override
    public void setMode(DisplayMode mode) {
        displayMode=mode;
    }

    /**
     *  更改list，即更改listview 的适配器
     *   (listview的适配器，就是填充内容只能为String[],所以我需要一个可以返回歌曲列表中所有歌曲名字的函数)
     * @param list
     */
    @Override
    public   ArrayAdapter<String>  setDisplayList(SongList list) {

        displayList=list;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.mainActivity,
                android.R.layout.simple_list_item_single_choice,
                list.getSongNames());
        return adapter;
    }

    @Override
    public void restartWith(String path,MediaPlayer mediaPlayer) {
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset();
            // 设置播放源
            mediaPlayer.setDataSource(path);
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
