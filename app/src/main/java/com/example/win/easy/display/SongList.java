package com.example.win.easy.display;

import android.widget.ArrayAdapter;

import com.example.win.easy.MainActivity;
import com.example.win.easy.song.Song;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongList {

    private String name;
    private List<Song> songList;
    private List<String> songNames;

    public static ArrayAdapter<String> toArrayAdapter(SongList songList){
        return new ArrayAdapter<>(
                MainActivity.mainActivity,
                android.R.layout.simple_list_item_single_choice,
                songList.getSongNames()
        );
    }

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

    public List<String> getSongNames(){
        if (songNames==null){
            songNames=new ArrayList<>();
            for (Song song:songList)
                songNames.add(song.getName());
        }
        return songNames;
    }
}
