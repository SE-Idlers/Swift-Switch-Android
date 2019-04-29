package com.example.win.easy.song;

import com.example.win.easy.persistence.component.FileSongMapConfigurationPersistence;
import com.example.win.easy.song.convert.File2SongConverterImpl;
import com.example.win.easy.song.interfaces.File2SongConverter;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.SongList;
import com.example.win.easy.songList.SongListMangerImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * i think this should just input file, and use file2song, then implement hashmap
 */
public class SongManagerImpl implements SongManager  {

    private File2SongConverter file2SongConverter = File2SongConverterImpl.getInstance();

    private static Map<File, Song> fileToSong;
    private static Map<Song, File> songToFile;
    private static List<File> files;
    private static List<Song> songs;
    private static List<List<Character>> sequences;

    private static SongManagerImpl instance = new SongManagerImpl();
    public static SongManagerImpl getInstance() { return instance; }
    private SongManagerImpl() {}

    static {
        fileToSong = FileSongMapConfigurationPersistence.getInstance().load();
        files = new ArrayList<>(fileToSong.keySet());
        songs = new ArrayList<>(fileToSong.values());
        sequences = new ArrayList<>();
        songToFile = new HashMap<>();
        for (File file : files) {
            songToFile.put(fileToSong.get(file), file);
            sequences.add(fileToSong.get(file).getSequence());
        }
    }

    @Override
    public Map<File, Song> getMap() {
        return fileToSong;
    }

    @Override
    public File toFile(Song song) {
        return songToFile.get(song);
    }

    @Override
    public Song toSong(File file) {
        return fileToSong.get(file);
    }

    @Override
    public Boolean add(File file) {
        fileToSong.put(file, file2SongConverter.convert(file));
        update();
        return true;
    }

    @Override
    public Boolean remove(File file) {
        if (!fileToSong.containsKey(file))
            return false;
        fileToSong.remove(file);
        update();
        return true;
    }

    @Override
    public Boolean addAll(List<File> fileList) {
        for (File file : fileList) {
            fileToSong.put(file, file2SongConverter.convert(file));
        }
        update();
        return true;
    }

    @Override
    public Boolean removeAll(List<File> fileList) {
        boolean allPresent = true;
        boolean allAbsent = true;
        for (File file : fileList) {
            if (!fileToSong.containsKey(file)) {
                allPresent = false;
                continue;
            }
            fileToSong.remove(file);
            allAbsent = false;
        }
        if (!allAbsent)
            update();
        return allPresent;
    }

    @Override
    public List<Song> selectSongsByIndices(List<Integer> indices) {
        List<Song> songsToSelect = new ArrayList<>();
        for (Integer integer : indices) {
            songsToSelect.add(songs.get(integer));
        }
        return songsToSelect;
    }

    @Override
    public List<List<Character>> getAllSequences() {
        return sequences;
    }

    @Override
    public List<Song> getAllSongs() {
        return songs;
    }

    private void update() {
        files = new ArrayList<>(fileToSong.keySet());
        songs = new ArrayList<>(fileToSong.values());
        songToFile = new HashMap<>();
        for (File file : files) {
            songToFile.put(fileToSong.get(file), file);
            sequences.add(fileToSong.get(file).getSequence());
        }
        List<SongList> songLists=SongListMangerImpl.getInstance().getAllSongLists();
        for (SongList songList:songLists)
            if(songList.getName().equals("默认歌单")) {
                songList.setSongList(songs);
                break;
            }
    }
}






