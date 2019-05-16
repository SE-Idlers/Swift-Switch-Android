package com.example.win.easy.song;

import com.example.win.easy.persistence.component.FileSongMapConfigurationPersistence;
import com.example.win.easy.song.convert.File2SongConverterImpl;
import com.example.win.easy.song.interfaces.File2SongConverter;
import com.example.win.easy.song.interfaces.SongManager;
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
    private static List<String> songNames;

    private static SongManagerImpl instance = new SongManagerImpl();
    public static SongManagerImpl getInstance() { return instance; }
    private SongManagerImpl() {}

    static {
        fileToSong = FileSongMapConfigurationPersistence.getInstance().load();
        if (fileToSong==null)
            fileToSong=new HashMap<>();
        update();
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
        if (fileToSong.containsKey(file))
            return true;
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
    public List<String> getNamesOfAllSongs() {
        return songNames;
    }

    @Override
    public List<Song> getAllSongs() {
        return songs;
    }

    private static void update() {
        files = new ArrayList<>(fileToSong.keySet());
        songs = new ArrayList<>(fileToSong.values());
        songToFile = new HashMap<>();
        sequences=new ArrayList<>();
        songNames=new ArrayList<>();
        for (File file : files) {
            Song song=fileToSong.get(file);
            songToFile.put(song, file);
            sequences.add(song.getSequence());
            songNames.add(song.getName());
        }
        SongListMangerImpl.getInstance().getDefaultSongList().setSongList(songs);
    }
}






