package com.example.win.easy;

import android.gesture.GestureOverlayView;
import android.graphics.Color;

public class ProxyGesture {

    private  GestureOverlayView[] gestures=new GestureOverlayView[4];
    private Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};

    public ProxyGesture(){
        for(int i=0;i<Constants.NumberOfGesture;i++){
            gestures[i]=MainActivity.mainActivity.findViewById(id[i]);
            init(gestures[i]);
        }
    }

    public void init(GestureOverlayView gesture) {
        ProxyGestureListener listener=new ProxyGestureListener();
        gesture.setGestureColor(Color.GREEN);
        gesture.setBackgroundColor(Color.GRAY);
        gesture.setGestureStrokeWidth(5);
        gesture.addOnGesturePerformedListener(listener);
    }
}





