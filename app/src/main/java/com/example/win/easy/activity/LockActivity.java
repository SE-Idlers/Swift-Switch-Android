package com.example.win.easy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.win.easy.Constants;
import com.example.win.easy.DashBoard;
import com.example.win.easy.DialogTool;
import com.example.win.easy.R;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.gesture.GestureProxy;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.TemporaryListGenerator;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.interfaces.SearchingView;
import com.example.win.easy.view.interfaces.SongListView;

import java.util.List;

public class LockActivity extends AppCompatActivity implements SongListView, SearchingView {

    public static LockActivity lockActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockActivity =this;
        RecognitionProxyWithFourGestures.getInstance().setAssetManager(getAssets());
        GestureProxy gestureProxy=GestureProxy.getInstance();
        getSupportFragmentManager();

        /////////////////////////
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        final ImageButton btnPrevious=LockActivity.lockActivity.findViewById(R.id.previous);
        final ImageButton btnNext=LockActivity.lockActivity.findViewById(R.id.next);

        //播放、暂停
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayManager.isPlaying()){
                    displayManager.pause();
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                }else {
                    displayManager.start();
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        //前一首
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.previous();
            }
        });
        //后一首
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.next();
            }
        });

        ///////////////////////////////////////////
        Button btnAddSong = LockActivity.lockActivity.findViewById(R.id.AddSong);
        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });
        Button buttonSeeAllSongs=LockActivity.lockActivity.findViewById(R.id.SeeAllSongs);
        buttonSeeAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockActivity.lockActivity.createDialogSeeAllSongs();
            }
        });

        ///////////////////////////////////////////
        Button btnAddSongList = LockActivity.lockActivity.findViewById(R.id.AddSongList);
        Button btnSeeSongList = LockActivity.lockActivity.findViewById(R.id.SeeSongList);
        btnAddSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockActivity.lockActivity.createDialogAddSong();
            }
        });
        btnSeeSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognitionProxyWithFourGestures.getInstance().clear();
                LockActivity.lockActivity.createDialogSeeSongList();
            }
        });

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



    private TemporaryListGenerator tool = TemporaryListGenerator.getInstance();
    private SongListManager songListManager=SongListMangerImpl.getInstance();
    private DashBoard dashBoard = LockActivity.lockActivity.findViewById(R.id.dash_board);

    @Override
    public void update(Song song) {
        //获取歌曲出现过的所有歌单
        List<SongList> appearanceLists =songListManager.appearanceListsOf(song);
        //设置dashBoard
        dashBoard.setup(appearanceLists, DashBoard.DashBoardType.SwitchSongList);
    }

    @Override
    public void update(List<Integer> sortedIndices) {
        //获得按来源得到的结果列表
        List<SongList> candidates = tool.toSearchResult(sortedIndices);
        //设置dashBoard
        dashBoard.setup(candidates, DashBoard.DashBoardType.SelectingSong);
    }


    ///////////////////////////////////////////////
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    public void updateBeginView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_play);
    }
    public void updatePauseView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    ///////////////////////////////////////////////
    ///////////////////////////////////////////////
    private SongManager songManager= SongManagerImpl.getInstance();

    public void createDialogSeeAllSongs(){
        DialogTool.createMenuDialog(
                LockActivity.lockActivity,
                "所有歌曲",
                songManager.getNamesOfAllSongs().toArray(new String[0]),
                null,
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 跳转到文件浏览页面，用于选择要添加的音乐文件
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        LockActivity.lockActivity.startActivityForResult(intent, Constants.READ_REQUEST_CODE);
    }


    ////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    /**
     * 查看歌单的对话框
     */
    private void createDialogSeeSongList(){
        DialogTool.createMenuDialog(
                LockActivity.lockActivity,
                "所有歌单",
                songListManager.getNameOfAllSongLists().toArray(new String[0]),
                new CheckSongListListener(),
                com.qmuiteam.qmui.R.style.QMUI_Dialog
        );
    }

    /**
     * 创建歌单的对话框
     */
    private void createDialogAddSong(){
        final EditText editText = new EditText(LockActivity.lockActivity);
        new AlertDialog.Builder(LockActivity.lockActivity)
                .setTitle("歌单名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String songListName=editText.getText().toString();
                        if (songListManager.containsSongListWithName(songListName)){
                            Toast.makeText(LockActivity.lockActivity.getApplicationContext(),"歌单 "+ songListName+"已存在",Toast.LENGTH_LONG).show();
                            return;
                        }
                        songListManager.add(new SongList(songListName));
                        Toast.makeText(LockActivity.lockActivity.getApplicationContext(),"歌单 "+ songListName+"已创建",Toast.LENGTH_LONG).show();
                        SongListConfigurationPersistence.getInstance()
                                .save(SongListMangerImpl.getInstance().getAllSongLists());
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    class CheckSongListListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            SongList songList=SongListMangerImpl.getInstance().getAllSongLists().get(which);
            DialogTool.createMenuDialog(
                    LockActivity.lockActivity,
                    songList.getName(),
                    songList.getSongNames().toArray(new String[0]),null,
                    com.qmuiteam.qmui.R.style.QMUI_Dialog
            );
        }
    }
}
