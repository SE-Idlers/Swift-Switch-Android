package com.example.win.easy.songList;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.win.easy.R;
import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.song.Song;
import com.example.win.easy.songList.interfaces.SongListManager;
import com.example.win.easy.view.DashboardView;

import java.util.ArrayList;
import java.util.List;

public class SongListMangerImpl implements SongListManager {

    private static SongListConfigurationPersistence songListConfigurationPersistence=SongListConfigurationPersistence.getInstance();
    private Button btnAddSongList = (Button) MainActivity.mainActivity.findViewById(R.id.AddSongList);
    private Button btnSeeSongList = (Button) MainActivity.mainActivity.findViewById(R.id.SeeSongList);


    private static SongListMangerImpl instance=new SongListMangerImpl();
    public static SongListMangerImpl getInstance(){return instance;}

    private static List<SongList> songLists=new ArrayList<>();

    private DashboardView dashboardView=DashboardView.getInstance();

  static {
      songLists=songListConfigurationPersistence.load();
      if(songLists==null)
          songLists=new ArrayList<>();
      songLists.add(new SongList("默认歌单"));
    }

    private SongListMangerImpl(){
        btnAddSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSong_Dialog(v);
            }
        });
        btnSeeSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeSongList_Dialog(v);
            }
        });
    }

    public  ArrayList<String> getAllNameOfList(){
       ArrayList<String> name=new ArrayList<String>();
        for (SongList songList : songLists) {
           name.add(songList.getName());
       }
       return name;
   }

   public SongList getSongListAtIndex(int index){
        return songLists.get(index);
   }

    public  List<SongList> getAllList(){
        return songLists;
    }


    public boolean add(SongList songList){return songLists.add(songList);}
    public boolean remove(SongList songList){return songLists.remove(songList);}

    /**
     * 改变某个歌单在所有歌单中的优先级
     * @param songList
     * @param indexTo
     */
    @Override
    public boolean changePriority(SongList songList, int indexTo) {
        if(!songLists.contains(songList))
            return false;
        songLists.remove(songList);
        songLists.add(indexTo,songList);
        return true;
    }

    /**
     * 返回包含某首歌曲的全部歌单
     * @param song
     * @return
     */
    @Override
    public List<SongList> appearanceListsOf(Song song) {
        List<SongList> lists=new ArrayList<>();
        for(SongList songList:songLists){
            if(songList.ifcontain(song))
                lists.add(songList);
        }
        return lists;
    }

    /**
     * 查看歌单的对话框
     */
    public void SeeSongList_Dialog(View view){
        final String[] itemArray = new String[getAllNameOfList().size()];
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(MainActivity.mainActivity);
       alertDialog.setItems(getAllNameOfList().toArray(itemArray),new DialogInterface.OnClickListener() {
           @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
           alertDialog.show();
        }

    /**
     * 创建歌单的对话框
     * @param view
     */
    public void AddSong_Dialog(View view){
        final EditText et = new EditText(MainActivity.mainActivity);
        new AlertDialog.Builder(MainActivity.mainActivity).setTitle("歌单名称")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        add(new SongList(et.getText().toString()));
                        Toast.makeText(MainActivity.mainActivity.getApplicationContext(),"歌单 "+ et.getText().toString()+"已创建",Toast.LENGTH_LONG).show();
                        if(songLists.size()<4)DashboardView.getInstance().AddTab(et.getText().toString());
                    }
                }).setNegativeButton("取消",null).show();
    }
}
