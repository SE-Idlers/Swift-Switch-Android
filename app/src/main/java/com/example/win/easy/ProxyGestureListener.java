package com.example.win.easy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.support.v4.app.ActivityCompat;

import com.example.win.easy.filter.CharSequenceFilterStrategy;
import com.example.win.easy.filter.FilterStrategy;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.song.SongManager;
import com.example.win.easy.song.SongManagerImpl;

import java.util.List;

public class ProxyGestureListener implements GestureOverlayView.OnGesturePerformedListener {


    RecognitionProxy recognitionProxy;
    FilterStrategy filterStrategy= CharSequenceFilterStrategy.getInstance();
    SongManager songManager= SongManagerImpl.getInstance();
    ProxyList proxyList=ProxyList.getInstance();
    public ProxyGestureListener(AssetManager assetManager){
        recognitionProxy=new RecognitionProxyWithFourGestures(assetManager);
    }

    public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
        //访问权限
        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            return;
        }

        List<Character> result=recognitionProxy.receive(PositionedImage.create(gesture,gesture.getID()));
        System.out.println(result);
        List<Integer> candidates=filterStrategy.filter(result,songManager.getAllSequences());
        proxyList.update(candidates);

    }
}
