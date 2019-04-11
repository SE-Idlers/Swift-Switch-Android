package com.example.win.easy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.util.ArrayList;

public class AdministrateSongs  {

    private static AdministrateSongs administrateSongs=new AdministrateSongs();

    private ArrayList<String> songPathList=new ArrayList<String>();//音乐绝对路径列表
    private ArrayList<String> songNameList=new ArrayList<String>();//音乐名称列表
    private File[] songFiles;
    String song_path = "";

    private AdministrateSongs(){
        CreatSongList();
    }

    /**
    *初始化歌曲列表
     * 1、songFiles,所有mp3文件路径,数据结构为File
     * 2、songPathList,mp3文件绝对路径，访问歌曲用这个
     * 3、songNameList,mp3文件名字，显示在列表中，paster()的输入
     */

    private void CreatSongList() {
        //访问权限
        if (ActivityCompat.checkSelfPermission(MainActivity.mainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.mainActivity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
            return;
        }

        File path = new File(Environment.getExternalStorageDirectory()+ "//Music//");      //获得SD卡的Music文件夹
        songFiles = path.listFiles(new MyFilter(".mp3"));//返回以.mp3结尾的文件 (自定义文件过滤)
        for (File file : songFiles) {
            songPathList.add(file.getAbsolutePath());   //获取文件的绝对路径,存入音乐列表中
            songNameList.add(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('c')+2,file.getAbsolutePath().indexOf('.')));
        }
    }

    public static AdministrateSongs getAdministrateSongs(){
        return administrateSongs;
    }

    public ArrayList<String> getSongPathlist(){ return songPathList; }

    public ArrayList<String> getSongNamelist(){ return songNameList; }

    public int getSonglistLongth(){
        return songPathList.size();
    }

    public String getOneOfSonglist(int position){ return songPathList.get(position); }

}

