package com.example.win.easy.display;

import com.example.win.easy.song.Song;

import java.util.List;

import lombok.Data;

@Data
public class SongList {

    private String name;
    private List<Song> songList;
    private List<String> songNames;

    public Song getSongAt(int index){return songList.get(index);}

    public boolean add(Song song){
        songList.add(song);
        songNames.add(song.getName());
        return true;
    }

    public boolean remove(Song song){
        songNames.remove(song.getName());
        return songList.remove(song);
    }

}
