package com.example.win.easy.recognization;

import android.gesture.Gesture;

import lombok.Builder;

@Builder
public class PositionedImage implements RecognizationUnit {

    public static PositionedImage create(Gesture gesture,int gestureId){
        return null;
    }

    Image image;

    int gestureId;

}
