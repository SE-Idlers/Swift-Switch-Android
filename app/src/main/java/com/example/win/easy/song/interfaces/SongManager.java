package com.example.win.easy.song.interfaces;

import com.example.win.easy.song.Song;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 这个接口实现了歌曲到关键码的对应以及关键码返回到歌曲
 * 可以删除歌曲，或者添加歌曲
 */
public interface SongManager {

    Map<File,Song> getMap();

    File toFile(Song song);

    Song toSong(File file);

    Boolean add(File file);

    Boolean remove(File file);

    Boolean addAll(List<File> files);

    Boolean removeAll(List<File> files);

    List<Song> selectSongsByIndices(List<Integer> indices);

    List<List<Character>> getAllSequences();

    List<Song> getAllSongs();
}
