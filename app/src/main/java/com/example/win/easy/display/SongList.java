package com.example.win.easy.display;

import com.example.win.easy.song.Song;

import java.util.List;

import lombok.Data;

@Data
public class SongList {

    String name;

    List<Song> content;
}
