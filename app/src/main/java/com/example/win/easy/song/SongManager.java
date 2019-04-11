package com.example.win.easy.song;

import java.io.File;
import java.util.List;

public interface SongManager {

    File toFile(Song song);

    Song toSong(File file);

    Boolean add(File file);

    Boolean remove(File file);

    Boolean addAll(List<File> files);

    Boolean removeAll(List<File> files);

    List<Song> selectSongsByIndices(List<Integer> indices);
}
