package com.example.win.easy.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.win.easy.Constants;
import com.example.win.easy.R;
import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.view.DashboardView;
import com.example.win.easy.view.SongPanelView;

public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        SwiftSwitchClassLoader.init();
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        GestureProxy gestureProxy=GestureProxy.getInstance();
        DashboardView dashboardView=DashboardView.getInstance();
        getSupportFragmentManager();

        startService(new Intent(this,MyService.class));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

      //  dashboardView.init();

       //File relative=new File("/ttt");
        //        File[] files=relative.listFiles();
        //
        //SongListMangerImpl songListManger=SongListMangerImpl.getInstance();
//      SongManagerImpl.getInstance().addAll(new ArrayList<>(Arrays.asList(files)));
    }

    @Override
    protected void onStop() {
        super.onStop();
//        SongListManager songListManager=SongListMangerImpl.getInstance();
//        SongManager songManager= SongManagerImpl.getInstance();
//        System.out.println("After switch all song lists: "+songListManager.getAllSongLists());
//        System.out.println("After switch all songs: "+songManager.getMap());
//        SongListConfigurationPersistence.getInstance().save(songListManager.getAllSongLists());
//        FileSongMapConfigurationPersistence.getInstance().save(songManager.getMap());
//        System.out.println(SongListConfigurationPersistence.getInstance().load());
//        System.out.println(FileSongMapConfigurationPersistence.getInstance().load());
    }

    /**
     * 添加歌曲文件且选取好要添加的歌曲文件后，会触发该函数，该函数用于跳转，让用户选择添加歌曲到哪一个歌单
     * @param requestCode 触发该函数的intent的请求码
     * @param resultCode 对该intent响应的响应码
     * @param resultData 如果成功操作，返回的数据
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData){

        //从查看文件夹Activity,即请求码为READ_REQUEST_CODE返回
        if (requestCode ==Constants.READ_REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                &&resultData!=null) {
            Uri uri = resultData.getData();
            SongPanelView.getInstance().createDialogAddSongToSongList(uri);
        }
    }
}
