package com.example.win.easy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProxyGestureListener implements GestureOverlayView.OnGesturePerformedListener {

    public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
        //访问权限
        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            return;
        }

        //存储路径
        String sdpath= Environment.getExternalStorageDirectory()+"/Pictures/";
        File path = new File(sdpath);
        File file =new File(sdpath,System.currentTimeMillis()+".jpg");

        //保存图片
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            Bitmap bitmap = gesture.toBitmap(28,28,10,0xffff0000);
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
            Toast.makeText(MainActivity.mainActivity , "已存", Toast.LENGTH_SHORT).show();
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
