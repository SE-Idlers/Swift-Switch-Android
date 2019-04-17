package com.example.win.easy.recognization;

import android.gesture.Gesture;
import android.graphics.Bitmap;


<<<<<<< HEAD
=======

>>>>>>> 21d3885fdbc0e99edab5864a7a13221a78e189e6
public class PositionedImage implements RecognizationUnit {

    private static Bitmap bitmap = null;
    private static float[] Float_array;
    private static int gestureId = 1;//默认为1

    public Bitmap getBitmap(){
        return bitmap;
    }
<<<<<<< HEAD
    private void setBitmap(Bitmap b) { bitmap = Bitmap.createBitmap(b); }//应改为private
    public float[] getFloat_array() { return Float_array; }
    private void setFloat_array(float[] fa) { Float_array = fa; }//应改为private
    public int getGestureId() { return gestureId; }
=======
    public void setBitmap(Bitmap b) { bitmap = Bitmap.createBitmap(b); }//应改为private
    public float[] getFloat_array(){ return Float_array; }
    public void setFloat_array(float[] fa){ Float_array = fa; }//应改为private
>>>>>>> 21d3885fdbc0e99edab5864a7a13221a78e189e6

    public static float[] Bitmap_to_FloatArray(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iarray = new int[28*28];
        bitmap.getPixels(iarray, 0, width, 0, 0, width, height);
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

    public static float[] create(Gesture gesture, int g){
        bitmap = gesture.toBitmap(28, 28, 10, 0xffff0000);
        return Float_array = Bitmap_to_FloatArray();
    }
}
