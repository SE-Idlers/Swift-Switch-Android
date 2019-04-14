package com.example.win.easy.display;

import com.example.win.easy.AdministrateSongs;
import com.example.win.easy.song.Song;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

public class ImplementSongListManger implements SongListManager {

    private ArrayList<SongList> songLists=new ArrayList<>();

    public void AddSongList(SongList list){songLists.add(list);}

    /**
     * 改变某个歌单在所有歌单中的优先级
     * @param songList
     * @param indexTo
     */
    @Override
    public void changePriority(SongList songList, int indexTo) {
        int preIndex=songLists.indexOf(songList);
        SongList templist=songList;
        if(indexTo<preIndex){//前移
            for(int i=preIndex;i>indexTo;i--) {
                songLists.set(i, songLists.get(i - 1));
            }
        }
        else {//后移
            for(int i=preIndex;i<indexTo;i++){
                songLists.set(i,songLists.get(i+1));
            }
        }
        songLists.set(indexTo,templist);
    }

    /**
     * 返回包含某首歌曲的全部歌单
     * @param song
     * @return
     */
    @Override
    public List<SongList> apperanceListsOf(Song song) {
        ArrayList<SongList> lists=new ArrayList<>();
        for(SongList songList:songLists){
            if(songList.getSongs().contains(song))lists.add(songList);
        }
        return lists;
    }
}
