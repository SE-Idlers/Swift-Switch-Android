package com.example.win.easy.tool;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongListTool {

    public static List<SongList> generateTempList(List<Integer> sortedIndices, List<SongPojo> allSongs){
        return generateSongLists(groupByDataSource(sortedIndices,allSongs));
    }

    private static List<SongPojo> selectSongsByIndices(List<Integer> indices,List<SongPojo> allSongs){
        List<SongPojo> songsToSelect = new ArrayList<>();
        for (Integer integer : indices)
            songsToSelect.add(allSongs.get(integer));
        return songsToSelect;
    }

    private static Map<DataSource,List<SongPojo>> groupByDataSource(List<Integer> sortedIndices, List<SongPojo> allSongs){
        Map<DataSource,List<SongPojo>> dataSourceGroup=new HashMap<>();//创建 来源-歌曲list 的map
        List<SongPojo> songPojos=selectSongsByIndices(sortedIndices,allSongs);//选出下标所指向的所有歌曲
        for (SongPojo songPojo:songPojos){
            if(!dataSourceGroup.containsKey(songPojo.getSource())){//对这个来源的第一首歌
                List<SongPojo> candidates=new ArrayList<>();
                candidates.add(songPojo);
                dataSourceGroup.put(songPojo.getSource(),candidates);//新建List并放入map
            }else
                dataSourceGroup.get(songPojo.getSource()).add(songPojo);
        }
        return dataSourceGroup;
    }

    private static List<SongList> generateSongLists(Map<DataSource,List<SongPojo>> dataSourceGroup){
        List<DataSource> dataSources=new ArrayList<>(dataSourceGroup.keySet());
        List<SongList> songLists=new ArrayList<>();//即将生成的歌单list
        for (DataSource dataSource:dataSources)
            songLists.add(
                    new SongList(
                            new SongListPojo(dataSource),
                            dataSourceGroup.get(dataSource)
                    )
            );
        return songLists;
    }
}
