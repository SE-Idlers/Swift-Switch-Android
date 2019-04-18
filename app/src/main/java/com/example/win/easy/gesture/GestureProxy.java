package com.example.win.easy.gesture;

import android.gesture.GestureOverlayView;
import android.graphics.Color;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.listener.HandwritingListener;

public class GestureProxy {

    private static GestureOverlayView[] gestures=new GestureOverlayView[Constants.NumberOfGesture];
    private static Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};
    private static GestureProxy instance=new GestureProxy();
    public static GestureProxy getInstance(){return instance;}
    private GestureProxy(){}

    static {
        for(int i=0;i<Constants.NumberOfGesture;i++){
            gestures[i]= MainActivity.mainActivity.findViewById(id[i]);
            init(gestures[i]);
        }
    }

    private static void init(GestureOverlayView gesture) {
        gesture.setGestureColor(Color.GREEN);
        gesture.setGestureStrokeWidth(15);
        gesture.addOnGesturePerformedListener(new HandwritingListener());
    }
}





