package com.example.win.easy.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.view.DashboardView;

import java.io.File;

public class MainActivity extends AppCompatActivity  {

    public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        GestureProxy gestureProxy=GestureProxy.getInstance();
        File relative=new File("/ttt");
        File[] files=relative.listFiles();
        SongManagerImpl songManager = SongManagerImpl.getInstance();
        DashboardView dashboardView=DashboardView.getInstance();getSupportFragmentManager();
      //  dashboardView.init();

       //
        //SongListMangerImpl songListManger=SongListMangerImpl.getInstance();
//      SongManagerImpl.getInstance().addAll(new ArrayList<>(Arrays.asList(files)));
    }

    /**
     * 从其他Activity返回时调用
     * @param requestCode
     * @param resultCode
     * @param resultData
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        //从查看文件夹Activity,即请求码为READ_REQUEST_CODE返回
        if (requestCode ==Constants.READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                SongManagerImpl.getInstance().AddToSongListDialog(uri);
            }
        }
    }
}
