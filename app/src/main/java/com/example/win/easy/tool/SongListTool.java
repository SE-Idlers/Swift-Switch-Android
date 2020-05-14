package com.example.win.easy.tool;

import com.example.win.easy.enumeration.DataSource;
import com.example.win.easy.db.SongDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SongListTool {

    public static List<SongListWithSongs> generateTempList(List<Integer> sortedIndices, List<SongDO> allSongs){
        return generateSongLists(groupByDataSource(sortedIndices,allSongs));
    }

    private static List<SongDO> selectSongsByIndices(List<Integer> indices, List<SongDO> allSongs){
        List<SongDO> songsToSelect = new ArrayList<>();
        for (Integer integer : indices)
            songsToSelect.add(allSongs.get(integer));
        return songsToSelect;
    }

    private static Map<DataSource,List<SongDO>> groupByDataSource(List<Integer> sortedIndices, List<SongDO> allSongs){
        Map<DataSource,List<SongDO>> dataSourceGroup=new HashMap<>();//创建 来源-歌曲list 的map
        List<SongDO> songDOs =selectSongsByIndices(sortedIndices,allSongs);//选出下标所指向的所有歌曲
        for (SongDO songDO : songDOs){
            if(!dataSourceGroup.containsKey(songDO.getSource())){//对这个来源的第一首歌
                List<SongDO> candidates=new ArrayList<>();
                candidates.add(songDO);
                dataSourceGroup.put(songDO.getSource(),candidates);//新建List并放入map
            }else
                dataSourceGroup.get(songDO.getSource()).add(songDO);
        }
        return dataSourceGroup;
    }

    private static List<SongListWithSongs> generateSongLists(Map<DataSource,List<SongDO>> dataSourceGroup){
        List<DataSource> dataSources=new ArrayList<>(dataSourceGroup.keySet());
        List<SongListWithSongs> songListWithSongs =new ArrayList<>();//即将生成的歌单list
        for (DataSource dataSource:dataSources)
            songListWithSongs.add(null
//                    new SongListWithSongs(
//                            SongListDO.builder().source(dataSource).build(),
//                            dataSourceGroup.get(dataSource)
//                    )
            );
        return songListWithSongs;
    }
}
