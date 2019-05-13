package com.example.win.easy.songList;

import com.example.win.easy.song.Song;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SongList {

    private String name="";
    private List<Song> songList=new ArrayList<>();
    private List<String> songNames=new ArrayList<>();

    public SongList(){}
    public SongList(String name){
        this.name=name;
    }

    public SongList(String name,List<Song> songs){
        this(name);
        addAll(songs);
    }
    public Song getSongAt(int index){return songList.get(index);}

    public boolean add(Song song){
        songList.add(song);
        songNames.add(song.getName());
        return true;
    }

    boolean contains(Song song){ return songList.contains(song); }

    public boolean remove(Song song){
        songNames.remove(song.getName());
        return songList.remove(song);
    }

    public void addAll(List<Song> songs){
        songList.addAll(songs);
        for (Song song:songList)
            songNames.add(song.getName());
    }

    public void setSongList(List<Song> songList){
        this.songList=songList;
        songNames.clear();
        for (Song song:songList)
            songNames.add(song.getName());
    }
}
