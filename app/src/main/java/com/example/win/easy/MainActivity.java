package com.example.win.easy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win.easy.song.SongManagerImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity  {

    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        ProxyMediaPlayer proxyMediaPlayer=ProxyMediaPlayer.getInstance();
        ProxyGesture proxyGestures=new ProxyGesture(getAssets());
        ProxyList proxyList=ProxyList.getInstance();
        File relative=new File("/ttt");
        File[] files=relative.listFiles();
        SongManagerImpl.getInstance().addAll(new ArrayList<>(Arrays.asList(files)));
    }
}
