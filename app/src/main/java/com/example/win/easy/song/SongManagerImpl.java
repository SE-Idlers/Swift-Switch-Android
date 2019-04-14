package com.example.win.easy.song;

import com.example.win.easy.persistence.component.FileSongMapConfigurationPersistence;
import com.example.win.easy.song.convert.File2SongConverter;
import com.example.win.easy.song.convert.File2SongConverterImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * i think this should just input file, and use file2song, then implement hashmap
 */
public class SongManagerImpl implements SongManager {

    private static FileSongMapConfigurationPersistence fileSongMapConfigurationPersistence=FileSongMapConfigurationPersistence.getInstance();
    private static File2SongConverter file2SongConverter =File2SongConverterImpl.getInstance();
    private static SongManagerImpl instance=new SongManagerImpl();

    private static Map<File, Song> fileToSong;
    private static Map<Song, File> songToFile;
    private static List<File> files;
    private static List<Song> songs;

    private SongManagerImpl(){}
    public static SongManagerImpl getInstance(){return instance;}
    static {
        fileToSong=fileSongMapConfigurationPersistence.load();
        if(fileToSong==null)
            fileToSong=new HashMap<>();
        files=new ArrayList<>(fileToSong.keySet());
        songs=new ArrayList<>(fileToSong.values());
        songToFile=new HashMap<>();
        for(File file:files){
            songToFile.put(fileToSong.get(file),file);
        }
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
        if(!fileToSong.containsKey(file))
            return false;
        fileToSong.remove(file);
        update();
        return true;
    }

    @Override
    public Boolean addAll(List<File> fileList) {
        for(File file : fileList) {
            fileToSong.put(file, file2SongConverter.convert(file));
        }
        update();
        return true;
    }

    @Override
    public Boolean removeAll(List<File> fileList) {
        boolean allPresent=true;
        boolean allAbsent=true;
        for (File file:fileList){
            if (!fileToSong.containsKey(file)){
                allPresent=false;
                continue;
            }
            fileToSong.remove(file);
            allAbsent=false;
        }
        if(!allAbsent)
            update();
        return allPresent;
    }

    @Override
    public List<Song> selectSongsByIndices(List<Integer> indices) {
        List<Song> songsToSelect=new ArrayList<>();
        for(Integer integer:indices){
            songsToSelect.add(songs.get(integer));
        }
        return songsToSelect;
    }

    private void update(){
        files=new ArrayList<>(fileToSong.keySet());
        songs=new ArrayList<>(fileToSong.values());
        songToFile=new HashMap<>();
        for(File file:files){
            songToFile.put(fileToSong.get(file),file);
        }
    }
}
