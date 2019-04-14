package com.example.win.easy.display.component;

import com.example.win.easy.display.SongList;
import com.example.win.easy.display.interfaces.SongListManager;
import com.example.win.easy.persistence.component.SongListConfigurationPersistence;
import com.example.win.easy.song.Song;

import java.util.ArrayList;
import java.util.List;

public class SongListMangerImpl implements SongListManager {

    private static SongListConfigurationPersistence songListConfigurationPersistence=SongListConfigurationPersistence.getInstance();
    private static SongListMangerImpl instance=new SongListMangerImpl();
    public static SongListMangerImpl getInstance(){return instance;}
    private SongListMangerImpl(){}

    private static List<SongList> songLists;

    static {
        songLists=songListConfigurationPersistence.load();
        if (songLists==null)
            songLists=new ArrayList<>();
    }

    public boolean add(SongList songList){return songLists.add(songList);}
    public boolean remove(SongList songList){return songLists.remove(songList);}

    /**
     * 改变某个歌单在所有歌单中的优先级
     * @param songList
     * @param indexTo
     */
    @Override
    public boolean changePriority(SongList songList, int indexTo) {
        if(!songLists.contains(songList))
            return false;
        songLists.remove(songList);
        songLists.add(indexTo,songList);
        return true;
    }

    /**
     * 返回包含某首歌曲的全部歌单
     * @param song
     * @return
     */
    @Override
    public List<SongList> appearanceListsOf(Song song) {
        List<SongList> lists=new ArrayList<>();
        for(SongList songList:songLists){
            if(songList.getSongList().contains(song))
                lists.add(songList);
        }
        return lists;
    }
}
