package com.example.win.easy.listener;

import android.Manifest;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;

import androidx.core.app.ActivityCompat;

import com.example.win.easy.activity.LockActivity;
import com.example.win.easy.filter.CharSequenceFilterStrategy;
import com.example.win.easy.filter.FilterStrategy;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.view.DashboardView;
import com.example.win.easy.view.interfaces.SearchingView;

import java.util.List;

public class HandwritingListener implements GestureOverlayView.OnGesturePerformedListener {


    private RecognitionProxy recognitionProxy=RecognitionProxyWithFourGestures.getInstance();
    private FilterStrategy<List<Character>> filterStrategy= CharSequenceFilterStrategy.getInstance();
    private SongManager songManager= SongManagerImpl.getInstance();
    private SearchingView searchingView= DashboardView.getInstance();
    private List<GestureOverlayView> onPerformedView = GestureProxy.getInstance().getAllGestures();

    public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
        //访问权限
        if (ActivityCompat.checkSelfPermission(LockActivity.lockActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LockActivity.lockActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            return;
        }

        //获得识别结果
        //获取当前正在输入的板的坐标: 0-3
        long index = onPerformedView.indexOf(gestureOverlayView);
        List<Character> result=recognitionProxy.receive(PositionedImage.create(gesture, index));
        System.out.println(result);
        //过滤得到备选歌曲的下标
        List<Integer> candidates=filterStrategy.filter(result,songManager.getAllSequences());
        //更新搜索结果视图
        searchingView.update(candidates);
    }
}
