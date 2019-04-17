package com.example.win.easy.recognization;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import com.example.win.easy.recognization.interfaces.RecognitionUnit;

public class Image implements RecognitionUnit {

    private static Bitmap bitmap = null;
    private static float[] Float_array;

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap b) { bitmap = Bitmap.createBitmap(b); }//应改为private
    public float[] getFloat_array(){ return Float_array; }
    public void setFloat_array(float[] fa){ Float_array = fa; }//应改为private

    public static float[] Bitmap_to_FloatArray(){
        int m_width = (int) bitmap.getWidth();
        int m_height = (int) bitmap.getHeight();
        int[] iarray = new int[28*28];
        bitmap.getPixels(iarray, 0, m_width, 0, 0, m_width, m_height);

        float[] farray = new float[28*28];
        /***
         *      A  R  G  B
         *  0x xx xx xx xx
         *  第一位符号位(binary)
         *  由于该bitmap中的RGB值均为0x000000（黑）或0xFFFFFF（白）
         *  有变化的只有A值，变化区间为
         *  0x80000000 -> 0x7FFFFFFF
         *  故对源代码做以下改动以达到图片的正确处理
         */
        for(int i =0;i<784;i++){
            if((float)iarray[i]/0x80000000 > 0.001f){
                farray[i] = (float)iarray[i] / 0x80000000;
            }
            else if((float)iarray[i]/0x7fffffff > 0.001f){
                farray[i] = (float)iarray[i] / 0x7fffffff;
            }
            else{
                farray[i] = 0.0f;
            }
        }
        return farray;
    }

    public static float[] create(Gesture gesture){
        bitmap = gesture.toBitmap(28, 28, 3, Color.WHITE);
        return Float_array = Bitmap_to_FloatArray();
    }
}
