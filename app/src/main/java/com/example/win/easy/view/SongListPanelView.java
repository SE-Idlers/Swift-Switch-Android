package com.example.win.easy.view;


import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.recognization.component.RecognitionProxyWithFourGestures;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;
import com.example.win.easy.songList.interfaces.SongListManager;

import java.util.List;

public class SongListPanelView {

    private static SongListPanelView instance=new SongListPanelView();
    public static SongListPanelView getInstance(){return instance;}
    private SongListPanelView(){}

    private SongListManager songListManager= SongListMangerImpl.getInstance();

    static {
        Button btnAddSongList = MainActivity.mainActivity.findViewById(R.id.AddSongList);
        Button btnSeeSongList = MainActivity.mainActivity.findViewById(R.id.SeeSongList);
        btnAddSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.createDialogAddSong();
            }
        });
        btnSeeSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecognitionProxyWithFourGestures.getInstance().clear();
                instance.createDialogSeeSongList();
            }
        });
    }

    /**
     * 查看歌单的对话框
     */
    private void createDialogSeeSongList(){
        List<String> songListNames=songListManager.getNameOfAllSongLists();
        new AlertDialog.Builder(MainActivity.mainActivity)
                .setItems(
                        songListNames.toArray(new String[songListNames.size()]),
                        null
                )
                .show();
    }

    /**
     * 创建歌单的对话框
     */
    private void createDialogAddSong(){
        final EditText editText = new EditText(MainActivity.mainActivity);
        new AlertDialog.Builder(MainActivity.mainActivity)
                .setTitle("歌单名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        String songListName=editText.getText().toString();
                        if (songListManager.containsSongListWithName(songListName)){
                            Toast.makeText(MainActivity.mainActivity.getApplicationContext(),"歌单 "+ songListName+"已存在",Toast.LENGTH_LONG).show();
                            return;
                        }
                        songListManager.add(new SongList(songListName));
                        Toast.makeText(MainActivity.mainActivity.getApplicationContext(),"歌单 "+ songListName+"已创建",Toast.LENGTH_LONG).show();
                        SongListConfigurationPersistence.getInstance()
                                .save(SongListMangerImpl.getInstance().getAllSongLists());
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
