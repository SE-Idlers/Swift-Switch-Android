package com.example.win.easy;

import android.content.res.AssetManager;
import android.gesture.GestureOverlayView;
import android.graphics.Color;

public class ProxyGesture {

    private  GestureOverlayView[] gestures=new GestureOverlayView[Constants.NumberOfGesture];
    private Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};
    private AssetManager assetManager;

    public ProxyGesture(AssetManager assetManager){
        this.assetManager=assetManager;
        for(int i=0;i<Constants.NumberOfGesture;i++){
            gestures[i]=MainActivity.mainActivity.findViewById(id[i]);
            init(gestures[i]);
        }
    }

    public void init(GestureOverlayView gesture) {
        ProxyGestureListener listener=new ProxyGestureListener(assetManager);
        gesture.setGestureColor(Color.GREEN);
        gesture.setGestureStrokeWidth(5);
        gesture.addOnGesturePerformedListener(listener);
    }
}





