package com.example.win.easy.display;

import com.example.win.easy.song.Song;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SongList {

    private  String name;
    List<Song> content;

    //这个地方我不太理解为什么不直接用arraylist,而用list,
    // ImplementListViewManager中有些函数要用这个，我先自己定义一下，不妥当的话，我在改。
    ArrayList<Song> songs;

    //以下是我自己添加的函数
    public ArrayList<String> getSongsName(){return null;}
    public ArrayList<Song> getSongs(){return songs;}
    public Song getSong(int index){return songs.get(index);}
    public String getName(){return name;}
}
