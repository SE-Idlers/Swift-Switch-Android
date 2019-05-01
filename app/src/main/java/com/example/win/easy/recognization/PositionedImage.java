package com.example.win.easy.recognization;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.win.easy.recognization.interfaces.RecognitionUnit;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PositionedImage implements RecognitionUnit {

    private static Bitmap bitmap = null;
    private static float[] Float_array;
    private static long gestureId;

    public float[] getFloat_array() { return Float_array; }
    public long getGestureId() { return gestureId; }

    public PositionedImage(Bitmap b, long g) { bitmap = b; gestureId = g;}
    public PositionedImage(){}//

    private static float[] Bitmap_to_FloatArray(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iarray = new int[28*28];
        bitmap.getPixels(iarray, 0, width, 0, 0, width, height);

        float[] farray = new float[28*28];
        /***
         *      A  R  G  B
         *  0x xx xx xx xx
         *  由于该bitmap中的RGB值仅有0x000000（黑）或0xFFFFFF（白）
         *  变化的只有透明度A值，00 - FF
         *  故做以下处理
         */
        for(int i =0;i<28*28;i++){
            float dealIt = iarray[i]>>>24;//舍弃RGB值，空位补0
            farray[i] = dealIt / 0x000000FF;
        }
        return farray;
    }

    public static PositionedImage create(Gesture gesture, long g){
        bitmap = gesture.toBitmap(28, 28, 3, Color.WHITE);
        Float_array = Bitmap_to_FloatArray();
        return new PositionedImage(bitmap, g);
    }
}
