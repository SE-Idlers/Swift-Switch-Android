package com.example.win.easy.activity;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.win.easy.ActivityHolder;
import com.example.win.easy.Constants;
import com.example.win.easy.DashBoard;
import com.example.win.easy.R;
import com.example.win.easy.application.SwiftSwitchApplication;
import com.example.win.easy.activity.interfaces.SearchingView;
import com.example.win.easy.activity.interfaces.SongListView;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.parser.filter.CharSequenceFilterStrategy;
import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.tool.SongList;
import com.example.win.easy.tool.SongListTool;
import com.example.win.easy.viewmodel.SimpleViewModel;

import java.util.ArrayList;
import java.util.List;

public class LockActivity extends AppCompatActivity implements SongListView, SearchingView {

    private DashBoard dashBoard = findViewById(R.id.dash_board);
    private ImageButton btnPause= findViewById(R.id.start);
    private ImageButton btnPrevious=findViewById(R.id.previous);
    private ImageButton btnNext=findViewById(R.id.next);
    private GestureOverlayView[] gestures=new GestureOverlayView[Constants.NumberOfGesture];
    private List<GestureOverlayView> Gestures = new ArrayList<>(Constants.NumberOfGesture);//to use indexOf
    private Integer[] id=new Integer[]{R.id.gesture1,R.id.gesture2,R.id.gesture3,R.id.gesture4};


    private SongListTool tool = SongListTool.getInstance();
    private DisplayManager displayManager= SwiftSwitchApplication.application.getAppComponent().getDisplayManager();
    private SimpleViewModel viewModel;
    private LiveData<List<List<Character>>> sequences;
    private LiveData<List<SongPojo>> allSongs;
    private LiveData<List<SongListPojo>> allSongLists;
    private LiveData<List<SongXSongList>> allRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        ActivityHolder.update(this);
        setContentView(R.layout.activity_main);
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        initButtons();
        initGestures();
        viewModel= ViewModelProviders.of(this).get(SimpleViewModel.class);
        LiveData<List<SongPojo>> allSongs=viewModel.getAllSongs();
        sequences= Transformations.map(allSongs, input -> {
            List<List<Character>> seqs=new ArrayList<>();
            for (SongPojo songPojo:input)
                seqs.add(songPojo.getSequence());
            return seqs;
        });
        sequences.observe(this, lists -> {});
        allSongs=viewModel.getAllSongs();
        allSongs.observe(this, songPojos -> {});
        allSongLists=viewModel.getAllSongLists();
        allSongLists.observe(this, songListPojos -> {});
        allRelation=viewModel.getAllRelation();
        allRelation.observe(this,songXSongLists -> {});
        dashBoard.setData(allSongs,allSongLists,allRelation);
    }


    @Override
    public void updateToSwitchingSongList(List<SongList> appearanceLists) {
        //设置dashBoard
        dashBoard.setup(appearanceLists, DashBoard.DashBoardType.SwitchSongList);
        //更新播放器效果
        updatePauseView();
    }

    @Override
    public void updateToSelectingSong(List<Integer> sortedIndices) {
        //获得按来源得到的结果列表
        List<SongList> candidates = tool.generateTempList(sortedIndices,allSongs.getValue());
        //设置dashBoard
        dashBoard.setup(candidates, DashBoard.DashBoardType.SelectingSong);
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
            gestures[i]= ActivityHolder.getLockActivity().get().findViewById(id[i]);
            Gestures.add(i, gestures[i]);//addBtn, set使用不规范可能会有bug
            gestures[i].setGestureColor(Color.GREEN);
            gestures[i].setBackgroundColor(getResources().getColor(R.color.app_color_blue));
            gestures[i].setGestureStrokeWidth(15);
            gestures[i].addOnGesturePerformedListener(new HandwritingListener(this,this));
        }
    }
    public List<GestureOverlayView> getAllGestures(){return Gestures;}
    private List<SongListPojo> appearanceListsOf(SongPojo songPojo){
        List<Long> songListIds=new ArrayList<>();
        List<SongListPojo> result=new ArrayList<>();
        if (allSongs.getValue()!=null&&allSongLists.getValue()!=null&&allRelation.getValue()!=null){
            for (SongXSongList songXSongList:allRelation.getValue())
                if (songXSongList.songId==songPojo.id)
                    songListIds.add(songXSongList.songListId);
            for (SongListPojo songListPojo:allSongLists.getValue())
                if (songListIds.contains(songListPojo.id))
                    result.add(songListPojo);
        }
        return result;
    }
    public class HandwritingListener implements GestureOverlayView.OnGesturePerformedListener {
        private RecognitionProxy recognitionProxy=RecognitionProxyWithFourGestures.getInstance();
        private FilterStrategy<List<Character>> filterStrategy= CharSequenceFilterStrategy.getInstance();
        private SearchingView searchingView;
        private List<GestureOverlayView> onPerformedView = getAllGestures();
        private LockActivity lockActivity;

        HandwritingListener(SearchingView searchingView, LockActivity lockActivity){
            this.searchingView=searchingView;
            this.lockActivity=lockActivity;
        }
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
}
