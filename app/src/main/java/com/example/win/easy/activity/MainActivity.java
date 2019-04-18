package com.example.win.easy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.win.easy.R;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;

import java.io.File;

public class MainActivity extends AppCompatActivity  {

    public static MainActivity mainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        GestureProxy gestureProxy=GestureProxy.getInstance();
        File relative=new File("/ttt");
        File[] files=relative.listFiles();
//        SongManagerImpl.getInstance().addAll(new ArrayList<>(Arrays.asList(files)));
    }
}
