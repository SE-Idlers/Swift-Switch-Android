package com.example.win.easy.gesture;

import android.gesture.GestureOverlayView;
import android.graphics.Color;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.listener.HandwritingListener;

import java.util.ArrayList;
import java.util.List;

public class GestureProxy {

    private static GestureOverlayView[] gestures=new GestureOverlayView[Constants.NumberOfGesture];
    private static List<GestureOverlayView> Gestures = new ArrayList<>(Constants.NumberOfGesture);//to use indexOf
    private static Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};
    private static GestureProxy instance=new GestureProxy();
    public static GestureProxy getInstance(){return instance;}
    private GestureProxy(){}

    static {
        for(int i=0;i<Constants.NumberOfGesture;i++){
            gestures[i]= MainActivity.mainActivity.findViewById(id[i]);
            Gestures.add(i, gestures[i]);//add, set使用不规范可能会有bug
            init(gestures[i]);
        }
    }

    private static void init(GestureOverlayView gesture) {
        gesture.setGestureColor(Color.GREEN);
        gesture.setBackgroundColor(MainActivity.mainActivity.getResources().getColor(R.color.app_color_blue));
        gesture.setGestureStrokeWidth(15);
        gesture.addOnGesturePerformedListener(new HandwritingListener());
    }
    public List<GestureOverlayView> getAllGestures(){return Gestures;}
}





