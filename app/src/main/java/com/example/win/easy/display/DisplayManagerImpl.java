package com.example.win.easy.display;

import android.media.MediaPlayer;

import com.example.win.easy.songList.SongList;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.song.SongManagerImpl;

import java.io.IOException;
import java.util.List;

public class DisplayManagerImpl implements DisplayManager {

    private SongManager songManager=SongManagerImpl.getInstance();
    private static DisplayManagerImpl instance=new DisplayManagerImpl();
    public static DisplayManagerImpl getInstance(){return instance;}
    private DisplayManagerImpl(){
        mediaPlayer=new MediaPlayer();
    }

    private MediaPlayer mediaPlayer;
    private SongList displayList;
    private DisplayMode displayMode;
    private Song currentSong;
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
        List<Song> songs=list.getSongList();
        if(songs.contains(currentSong)){
            currentSongIndex=songs.indexOf(currentSong);
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
    public boolean restartWith(Song song,SongList listSongAt) {
        //设置当前歌曲
        currentSong=song;
        //开始播放
        display(song,mediaPlayer);
        //设置播放列表，同时更新index
        return setDisplayList(listSongAt);
    }

    private void display(Song song,MediaPlayer mediaPlayer){
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset();
            // 设置播放源
            mediaPlayer.setDataSource(songManager.toFile(song).getAbsolutePath());
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void next(MediaPlayer mediaPlayer){
        currentSongIndex=++currentSongIndex%displayList.getSongList().size();
        displayByIndex(currentSongIndex,mediaPlayer);
    }


    private void previous(MediaPlayer mediaPlayer){
        currentSongIndex=--currentSongIndex%displayList.getSongList().size();
        displayByIndex(currentSongIndex,mediaPlayer);
    }

    private void displayByIndex(int songIndex , MediaPlayer mediaPlayer) {
        //设置当前主体
        currentSong=displayList.getSongAt(songIndex);
        //播放
        display(currentSong,mediaPlayer);
    }

}
