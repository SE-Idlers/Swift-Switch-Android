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

    private static SongListMangerImpl instance=new SongListMangerImpl();
    public static SongListMangerImpl getInstance(){return instance;}
    private SongListMangerImpl(){ }

    static {
      songLists=SongListConfigurationPersistence.getInstance().load();
      if (songLists==null)
          songLists=new ArrayList<>();
      if (songLists.size()==0)
        songLists.add(new SongList("默认歌单",songManager.getAllSongs()));
    }


    @Override
    public List<String> getNameOfAllSongLists(){
        List<String> names=new ArrayList<>();
        for (SongList songList : songLists) {
           names.add(songList.getName());
        }
        return names;
    }
    @Override
    public List<SongList> getAllSongLists(){ return songLists; }
    @Override
    public SongList getSongListAt(int index){ return songLists.get(index); }
    @Override
    public boolean add(SongList songList){return songLists.add(songList);}
    @Override
    public boolean remove(SongList songList){return songLists.remove(songList);}
    @Override
    public int size() { return songLists.size(); }
    @Override
    public boolean changePriority(SongList songList, int indexTo) {
        if(!songLists.contains(songList)||indexTo>=songLists.size()||indexTo<0)
            return false;
        songLists.remove(songList);
        songLists.add(indexTo,songList);
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
}
