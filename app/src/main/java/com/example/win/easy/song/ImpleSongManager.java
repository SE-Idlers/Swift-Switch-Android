package com.example.win.easy.song;

import com.example.win.easy.persistence.FileSongMapConfigurationPersistence;
import com.example.win.easy.persistence.SongListConfigurationPersistence;
import com.example.win.easy.song.convert.File2SongConverter;
import com.example.win.easy.song.convert.SongInfList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * i think this should just input file, and use file2song, then implement hashmap
 */
public class ImpleSongManager implements SongManager {

    private static FileSongMapConfigurationPersistence fileSongMapConfigurationPersistence;
    private static Map<File,Song> loadFileAndSong = fileSongMapConfigurationPersistence.load();
    private HashMap<File, Song> fileToSong = new HashMap<>();
    private HashMap<Song, File> songToFile = new HashMap<>();
    private List<File> files = new ArrayList<>();

    private static SongListConfigurationPersistence songListConfigurationPersistence;
    private static List<Song> songs = (List<Song>) loadFileAndSong.values();  //I need song list
    private File2SongConverter file2SongConverter = new SongInfList();

    public ImpleSongManager(){
        fileToSong.putAll(loadFileAndSong);
        List<File> fileList = (List<File>) loadFileAndSong.keySet();

        for(File file : fileList)
        {
            songToFile.put(file2SongConverter.convert(file) ,file);
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
        files.add(file);
        fileToSong.put(file, file2SongConverter.convert(file));
        songToFile.put(file2SongConverter.convert(file), file);
        return null;
    }

    @Override
    public Boolean remove(File file) {
        if(files.isEmpty())
            return false;
        else
        {
            files.remove(file);
            fileToSong.remove(file);
            songToFile.remove(file2SongConverter.convert(file));
            return true;
        }
    }

    @Override
    public Boolean addAll(List<File> files) {
        this.files = files;
        for(File file : files)
        {
            fileToSong.put(file, file2SongConverter.convert(file));
            songToFile.put(file2SongConverter.convert(file) ,file);
        }
        return null;
    }

    @Override
    public Boolean removeAll(List<File> files) {
        if(files.isEmpty())
            return false;
        else
        {   for(File file : files)
            {
                files.remove(file);
                fileToSong.remove(file);
                songToFile.remove(file2SongConverter.convert(file));
            }
            return true;
        }
    }

    @Override
    //this is a very very funny thing!.
    public List<Song> selectSongsByIndices(List<Integer> indices) {
        return null;
    }
}
