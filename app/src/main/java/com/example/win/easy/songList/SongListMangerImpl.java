package com.example.win.easy.songList;

import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.SongManagerImpl;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.songList.interfaces.SongListManager;

import java.util.ArrayList;
import java.util.List;

public class SongListMangerImpl implements SongListManager {

    private static SongManager songManager= SongManagerImpl.getInstance();
    private static List<SongList> songLists;
    private static List<String> songListNames;
    private static SongList defaultSongList;

    private static SongListMangerImpl instance=new SongListMangerImpl();
    public static SongListMangerImpl getInstance(){return instance;}
    private SongListMangerImpl(){ }

    static {
      songLists=SongListConfigurationPersistence.getInstance().load();
      if (songLists==null)
          songLists=new ArrayList<>();
      if (songLists.size()==0){
          defaultSongList=new SongList("默认歌单");
          defaultSongList.setSongList(songManager.getAllSongs());
          songLists.add(defaultSongList);
      }else
          defaultSongList=songLists.get(0);
      update();
    }


    @Override
    public List<String> getNameOfAllSongLists(){
        return songListNames;
    }

    @Override
    public List<String> getNameOfAllSelfDefinedSongLists() {
        return songListNames.subList(1,songListNames.size());
    }

    @Override
    public List<SongList> getAllSongLists(){ return songLists; }

    @Override
    public List<SongList> getAllSelfDefinedSongLists() {
        return songLists.subList(1,songLists.size());
    }

    @Override
    public SongList getSongListAt(int index){ return songLists.get(index); }

    @Override
    public boolean containsSongListWithName(String songListName) {
        for (SongList songList:songLists){
            if (songList.getName().equals(songListName))
                return true;
        }
        return false;
    }

    @Override
    public boolean add(SongList songList){
        if(songLists.contains(songList))
            return true;
        songListNames.add(songList.getName());
        return songLists.add(songList);
    }
    @Override
    public boolean remove(SongList songList){
        if(!songLists.contains(songList))
            return false;
        songListNames.remove(songList.getName());
        return songLists.remove(songList);
    }
    @Override
    public int size() { return songLists.size(); }
    @Override
    public boolean changePriority(SongList songList, int indexTo) {
        if(!songLists.contains(songList)||indexTo>=songLists.size()||indexTo<0)
            return false;
        songLists.remove(songList);
        songLists.add(indexTo,songList);
        update();
        return true;
    }
    @Override
    public List<SongList> appearanceListsOf(Song song) {
        List<SongList> lists=new ArrayList<>();
        for(SongList songList:songLists){
            if(songList.contains(song))
                lists.add(songList);
        }
        return lists;
    }

    @Override
    public SongList getDefaultSongList() {
        return defaultSongList;
    }

    private static void update(){
        if(songListNames==null)
            songListNames=new ArrayList<>();
        songListNames.clear();
        for (SongList songList : songLists) {
            songListNames.add(songList.getName());
        }
    }

}
