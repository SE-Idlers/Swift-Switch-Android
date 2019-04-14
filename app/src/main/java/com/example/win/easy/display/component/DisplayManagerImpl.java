package com.example.win.easy.display.component;

import android.media.MediaPlayer;

import com.example.win.easy.display.DisplayMode;
import com.example.win.easy.display.SongList;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.SongManager;
import com.example.win.easy.song.SongManagerImpl;

import java.io.IOException;

public class DisplayManagerImpl implements DisplayManager {

    private SongManager songManager=SongManagerImpl.getInstance();
    private static DisplayManagerImpl instance=new DisplayManagerImpl();
    public static DisplayManagerImpl getInstance(){return instance;}
    private DisplayManagerImpl(){}

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
        restartWith(displayList.getSongAt(songIndex),mediaPlayer);
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
    public void setDisplayList(SongList list) {
        displayList=list;
    }

    @Override
    public void restartWith(Song song,MediaPlayer mediaPlayer) {
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset();
            // 设置播放源
            mediaPlayer.setDataSource(songManager.toFile(song).getAbsolutePath().toString());
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
