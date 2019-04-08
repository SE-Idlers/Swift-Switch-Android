package com.example.win.easy;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class ProxyMediaPlayer {

    private  static  ProxyMediaPlayer proxyMediaPlayer=new ProxyMediaPlayer();

    private AdministrateSongs administrateSongs=AdministrateSongs.getAdministrateSongs();
    private ProxyList proxyList=ProxyList.getProxyList();

    private MediaPlayer mediaPlayer=new MediaPlayer();
    private int currentPosition;//当前音乐播放的进度
    private String song_path="";

    private final ImageButton btnPause = (ImageButton) MainActivity.mainActivity.findViewById(R.id.start);
    private final ImageButton btnPrevious = (ImageButton) MainActivity.mainActivity.findViewById(R.id.previous);
    private final ImageButton btnNext = (ImageButton) MainActivity.mainActivity.findViewById(R.id.next);

    private ProxyMediaPlayer(){
        AdministrateButton();
        AdministrateList();
    }

   public static ProxyMediaPlayer getProxyMediaPlayer(){
       return proxyMediaPlayer;
    }

    private  void AdministrateButton() {
        //播放、暂停
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (song_path.isEmpty())
                    Toast.makeText(MainActivity.mainActivity, "先选收歌曲先听听", Toast.LENGTH_SHORT).show();
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
                changeMusic(--currentPosition);
            }
        });
        //后一首
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMusic(++currentPosition);
            }
        });
    }

    private  void AdministrateList(){
        proxyList.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                proxyMediaPlayer.changeMusic(position);
            }
        });
    }

    public void changeMusic(int position) {
        if(!mediaPlayer.isPlaying()) btnPause.setImageResource(android.R.drawable.ic_media_pause);
        if (position < 0) {
            //比第一首在前一首
            currentPosition = position = administrateSongs.getSonglistLongth() - 1;
        } else if (position > administrateSongs.getSonglistLongth() - 1) {
            //最后一首的下一首
            currentPosition = position = 0;
        }else {
            currentPosition=position;
        }
        song_path = administrateSongs.getOneOfSonglist(currentPosition);
        try {
            // 切歌之前先重置，释放掉之前的资源
            mediaPlayer.reset();
            // 设置播放源
            mediaPlayer.setDataSource(song_path);
            // 开始播放前的准备工作，加载多媒体资源，获取相关信息
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

