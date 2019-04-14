package com.example.win.easy;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.win.easy.display.component.DisplayManagerImpl;
import com.example.win.easy.song.Song;

public class ProxyMediaPlayer  {
    private  static  ProxyMediaPlayer proxyMediaPlayer=new ProxyMediaPlayer();
    private DisplayManagerImpl implementDisplayManager=DisplayManagerImpl.getInstance();

    private MediaPlayer mediaPlayer=new MediaPlayer();
    private int currentPosition;//当前音乐播放的下标
    private String song_path="";
    private Song currentSong=new Song();

    private final ImageButton btnPause = (ImageButton) MainActivity.mainActivity.findViewById(R.id.start);
    private final ImageButton btnPrevious = (ImageButton) MainActivity.mainActivity.findViewById(R.id.previous);
    private final ImageButton btnNext = (ImageButton) MainActivity.mainActivity.findViewById(R.id.next);

    private ProxyMediaPlayer(){
        AdministrateButton();
    }

    private  void AdministrateButton() {
        //播放、暂停
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song_path.isEmpty())
                    Toast.makeText(MainActivity.mainActivity, "先选首歌曲听听", Toast.LENGTH_SHORT).show();
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();  //暂停
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                } else if (!song_path.isEmpty()) {
                    mediaPlayer.start();   //继续播放
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        //前一首
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()) btnPause.setImageResource(android.R.drawable.ic_media_pause);
                implementDisplayManager.previous(--currentPosition,mediaPlayer);}
        });
        //后一首
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()) btnPause.setImageResource(android.R.drawable.ic_media_pause);
                implementDisplayManager.next(++currentPosition,mediaPlayer); }
        });
    }

    public static ProxyMediaPlayer getProxyMediaPlayer(){
       return proxyMediaPlayer;
    }

    public int getCurrentPosition(){return currentPosition;}
    public MediaPlayer getmediaPlayer(){
        return mediaPlayer;
    }


}

