package com.example.win.easy.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.win.easy.ActivityHolder;
import com.example.win.easy.Constants;
import com.example.win.easy.Dashboard;
import com.example.win.easy.R;
import com.example.win.easy.view.activity.interfaces.SearchingView;
import com.example.win.easy.view.activity.interfaces.SongListView;
import com.example.win.easy.dagger.SwiftSwitchApplication;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.factory.ListenerFactory;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.tool.SongList;
import com.example.win.easy.tool.SongListTool;
import com.example.win.easy.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import lombok.AllArgsConstructor;

public class LockActivity extends AppCompatActivity implements SongListView, SearchingView {

    @BindView(R.id.dash_board) Dashboard dashboard;
    @BindView(R.id.start) ImageButton btnPause;
    @BindView(R.id.previous) ImageButton btnPrevious;
    @BindView(R.id.next) ImageButton btnNext;
    private GestureOverlayView[] gestures=new GestureOverlayView[Constants.NumberOfGesture];
    private List<GestureOverlayView> Gestures = new ArrayList<>(Constants.NumberOfGesture);//to use indexOf
    private Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};

    @Inject DisplayManager displayManager;
    @Inject ViewModelProvider.Factory factory;
    @Inject RecognitionProxy recognitionProxy;
    @Inject FilterStrategy<List<Character>> filterStrategy;
    @Inject ListenerFactory listenerFactory;
    private SimpleViewModel viewModel;
    private LiveData<List<List<Character>>> sequences;
    private LiveData<List<SongDO>> allSongs;
    private LiveData<List<SongListDO>> allSongLists;
    private LiveData<List<SongXSongListDO>> allRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        ActivityHolder.update(this);
        setContentView(R.layout.activity_dashboard);
        SwiftSwitchApplication.application.getDashboardComponent(this).inject(this);
        viewModel= ViewModelProviders.of(this,factory).get(SimpleViewModel.class);
        recognitionProxy.setAssetManager(getAssets());
        dashboard.setListenerFactory(listenerFactory);

        initButtons();
        initGestures();

        LiveData<List<SongDO>> allSongs=viewModel.getAllSongs();
        sequences= Transformations.map(allSongs, input -> {
            List<List<Character>> seqs=new ArrayList<>();
            for (SongDO songDO :input)
                seqs.add(songDO.getSequence());
            return seqs;
        });
        sequences.observe(this, lists -> {});
        allSongs=viewModel.getAllSongs();
        allSongs.observe(this, songPojos -> {});
        allSongLists=viewModel.getAllSongLists();
        allSongLists.observe(this, songListPojos -> {});
        allRelation=viewModel.getAllRelation();
        allRelation.observe(this,songXSongLists -> {});
        dashboard.setData(allSongs,allSongLists,allRelation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwiftSwitchApplication.application.clearDashboardComponent();
    }


    @Override
    public void updateToSwitchingSongList(List<SongList> appearanceLists) {
        //设置dashBoard
        dashboard.setup(appearanceLists, Dashboard.DashboardType.SwitchSongList);
        //更新播放器效果
        updatePauseView();
    }

    @Override
    public void updateToSelectingSong(List<Integer> sortedIndices) {
        //获得按来源得到的结果列表
        List<SongList> candidates = SongListTool.generateTempList(sortedIndices,allSongs.getValue());
        //设置dashBoard
        dashboard.setup(candidates, Dashboard.DashboardType.SelectingSong);
    }

    /**
     * 更新到开始形状
     */
    public void updateBeginView(){
        btnPause.setImageResource(android.R.drawable.ic_media_play);
    }

    /**
     * 更新到暂停形状
     */
    public void updatePauseView(){
        btnPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    /**
     * 初始化播放控制的按钮
     */
    private void initButtons(){
        //播放、暂停
        btnPause.setOnClickListener(v -> {
            if (displayManager.isPlaying()){
                displayManager.pause();
                updateBeginView();
            }else {
                displayManager.start();
                updatePauseView();
            }
        });
        //前一首
        btnPrevious.setOnClickListener(v -> {
            if(!displayManager.isPlaying())
                updatePauseView();
            displayManager.previous();
        });
        //后一首
        btnNext.setOnClickListener(v -> {
            if(!displayManager.isPlaying())
                updatePauseView();
            displayManager.next();
        });
    }

    /**
     * 初始化手写板
     */
    private void initGestures(){
        for(int i=0;i<Constants.NumberOfGesture;i++){
            gestures[i]= findViewById(id[i]);
            Gestures.add(i, gestures[i]);//addBtn, set使用不规范可能会有bug
            gestures[i].setGestureColor(Color.GREEN);
            gestures[i].setBackgroundColor(getResources().getColor(R.color.app_color_blue));
            gestures[i].setGestureStrokeWidth(15);
            gestures[i].addOnGesturePerformedListener(create());
        }
    }

    @AllArgsConstructor
    public class HandwritingListener implements GestureOverlayView.OnGesturePerformedListener {
        private RecognitionProxy recognitionProxy;
        private FilterStrategy<List<Character>> filterStrategy;
        private SearchingView searchingView;
        private List<GestureOverlayView> onPerformedView;
        private LockActivity lockActivity;

        public void onGesturePerformed(GestureOverlayView gestureOverlayView, final Gesture gesture) {
            //访问权限
            if (ActivityCompat.checkSelfPermission(lockActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(lockActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                return;
            }
            //获得识别结果
            //获取当前正在输入的板的坐标: 0-3
            long index = onPerformedView.indexOf(gestureOverlayView);
            List<Character> result=recognitionProxy.receive(PositionedImage.create(gesture, index));
            System.out.println(result);
            //过滤得到备选歌曲的下标
            List<Integer> candidates=filterStrategy.filter(result,sequences.getValue());
            //更新搜索结果视图
            searchingView.updateToSelectingSong(candidates);
        }
    }

    private HandwritingListener create(){
        return new HandwritingListener(recognitionProxy,filterStrategy,this,Gestures,this);
    }
}
