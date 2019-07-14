package com.example.win.easy.display;

import android.media.MediaPlayer;

import com.example.win.easy.tool.SongList;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.repository.db.pojo.SongPojo;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DisplayManagerImpl implements DisplayManager {

    @Inject
    public DisplayManagerImpl(MediaPlayer mediaPlayer){
        this.mediaPlayer=mediaPlayer;
    }

    private MediaPlayer mediaPlayer;
    private SongList displayList;
    private DisplayMode displayMode;
    private SongPojo currentSong;
    private int currentSongIndex;

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }


    //这三个函数我先占个位，没想好怎么去实现
    private void random(){}
    private void sample(){}
    private void automata(){}

    @Override
    public SongList getDisplayList(){return displayList;}

    @Override
    public void next(){
        next(mediaPlayer);
    }

    @Override
    public void previous(){
        previous(mediaPlayer);
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void start() {
        if(currentSong!=null)
            mediaPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    @Override
    public void setMode(DisplayMode mode) {
        displayMode=mode;
    }

    @Override
    public boolean setDisplayList(SongList list) {
        List<SongPojo> songPojos=list.getSongPojos();
        if(songPojos.contains(currentSong)){
            this.displayList=list;
            currentSongIndex=songPojos.indexOf(currentSong);
            return true;
        }
        return false;
    }

    @Override
    public void displayByIndex(int index) {
        currentSongIndex=index;
        displayByIndex(index,mediaPlayer);
    }

    @Override
    public boolean restartWith(SongPojo songPojo, SongList listSongAt) {
        //设置当前歌曲
        currentSong=songPojo;
        //开始播放
        display(songPojo,mediaPlayer);
        //设置播放列表，同时更新index
        return setDisplayList(listSongAt);
    }

    private void display(SongPojo songPojo,MediaPlayer mediaPlayer){
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset();
            // 设置播放源
            mediaPlayer.setDataSource(songPojo.songPath);
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void next(MediaPlayer mediaPlayer){
        currentSongIndex=++currentSongIndex%displayList.getSongPojos().size();
        displayByIndex(currentSongIndex,mediaPlayer);
    }


    private void previous(MediaPlayer mediaPlayer){
        currentSongIndex=--currentSongIndex%displayList.getSongPojos().size();
        displayByIndex(currentSongIndex,mediaPlayer);
    }

    private void displayByIndex(int songIndex , MediaPlayer mediaPlayer) {
        //设置当前主体
        currentSong=displayList.getSongPojos().get(songIndex);
        //播放
        display(currentSong,mediaPlayer);
    }

}
