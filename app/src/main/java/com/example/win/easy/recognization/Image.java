package com.example.win.easy.recognization;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Image implements RecognizationUnit {

    private static Bitmap bitmap = null;
    private static float[] Float_array;

    public Bitmap getBitmap(){
        return bitmap;
    }
    public void setBitmap(Bitmap b) { bitmap = Bitmap.createBitmap(b); }//应改为private
    public float[] getFloat_array(){ return Float_array; }
    public void setFloat_array(float[] fa){ Float_array = fa; }//应改为private

    public static float[] Bitmap_to_FloatArray(){
        //重构大小
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) 28) / width;
        float scaleHeight = ((float) 28) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
                (int) height, matrix, true);
        //转换
        int m_width = (int) bitmap.getWidth();
        int m_height = (int) bitmap.getHeight();
        int[] iarray = new int[28*28];
        bitmap.getPixels(iarray, 0, m_width, 0, 0, m_width, m_height);
        float[] farray = new float[28*28];
        for(int i =0;i<784;i++){
            if(((float)iarray[i]/-16777216.0f > 0.001f)){
                farray[i] = ((float) iarray[i]) / -16777216.0f;
            }
            else{
                farray[i] = 0.0f;
            }
        }
        return farray;
    }

    public static float[] create(Gesture gesture){
        bitmap = gesture.toBitmap(28, 28, 10, 0xffff0000);
        return Float_array = Bitmap_to_FloatArray();
    }
}
