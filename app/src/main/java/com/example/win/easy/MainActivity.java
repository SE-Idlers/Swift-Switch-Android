package com.example.win.easy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
    }
}
