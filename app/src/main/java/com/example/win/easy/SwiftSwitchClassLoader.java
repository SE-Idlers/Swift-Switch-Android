package com.example.win.easy;

import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.songList.SongListMangerImpl;

//防止部分类懒惰加载，提前实例化静态变量
public class SwiftSwitchClassLoader {

    private static SwiftSwitchClassLoader instance=new SwiftSwitchClassLoader();
    public static SwiftSwitchClassLoader getInstance(){return instance;}
    private SwiftSwitchClassLoader(){}

    public static void init(){
        SongManagerImpl.getInstance();
        SongListMangerImpl.getInstance();
    }
}
