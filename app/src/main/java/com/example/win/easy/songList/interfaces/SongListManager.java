package com.example.win.easy.songList.interfaces;

import com.example.win.easy.song.Song;
import com.example.win.easy.songList.SongList;

import java.util.List;

public interface SongListManager {

    /**
     * 改变某个歌单在所有歌单中的优先级
     * @param songList 要改变的歌单
     * @param indexTo 将改变到的优先级位置
     * @return 成功操作时返回true,歌单不存在或者下标不合法返回false
     */
    boolean changePriority(SongList songList, int indexTo);

    /**
     * 返回包含某首歌曲的全部歌单
     * @param song 查找歌单的歌曲
     * @return 歌曲出现过所有歌单组成的列表
     */
    List<SongList> appearanceListsOf(Song song);

    SongList getDefaultSongList();

    List<SongList> getAllSongLists();

    List<SongList> getAllSelfDefinedSongLists();

    List<String> getNameOfAllSongLists();

    List<String> getNameOfAllSelfDefinedSongLists();

    SongList getSongListAt(int index);

    boolean containsSongListWithName(String songListName);

    boolean add(SongList songList);

    boolean remove(SongList songList);

    int size();

}
