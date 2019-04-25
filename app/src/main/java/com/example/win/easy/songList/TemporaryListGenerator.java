package com.example.win.easy.songList;

import com.example.win.easy.Constants;
import com.example.win.easy.song.DataSource;
import com.example.win.easy.song.Song;
import com.example.win.easy.song.interfaces.SongManager;
import com.example.win.easy.song.SongManagerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporaryListGenerator {

    private SongManager songManager= SongManagerImpl.getInstance();

    public static TemporaryListGenerator getInstance(){
        return instance;
    }
    private static TemporaryListGenerator instance =new TemporaryListGenerator();
    private TemporaryListGenerator(){}

    public List<SongList> toSearchResult(List<Integer> sortedIndices){
        return generateSongLists(groupByDataSource(sortedIndices));
    }
    private Map<DataSource,List<Song>> groupByDataSource(List<Integer> sortedIndices){
        Map<DataSource,List<Song>> dataSourceGroup=new HashMap<>();//创建 来源-歌曲list 的map
        List<Song> songs=songManager.selectSongsByIndices(sortedIndices);//选出下标所指向的所有歌曲
        for (Song song:songs){
            if(!dataSourceGroup.containsKey(song.getSource())){//对这个来源的第一首歌
                List<Song> candidates=new ArrayList<>();
                candidates.add(song);
                dataSourceGroup.put(song.getSource(),candidates);//新建List并放入map
            }else
                dataSourceGroup.get(song.getSource()).add(song);
        }
        return dataSourceGroup;
    }

    private List<SongList> generateSongLists(Map<DataSource,List<Song>> dataSourceGroup){
        List<DataSource> dataSources=new ArrayList<>(dataSourceGroup.keySet());

        //来源超过可显示tab上限时截断，保留最靠前的
        if(dataSources.size()> Constants.NumberOfList)
            dataSources=dataSources.subList(0,Constants.NumberOfList);

        int dataSourceAmount=dataSources.size();//来源数量
        List<SongList> songLists=new ArrayList<>(dataSourceAmount);//即将生成的歌单list

        for (int dataSourceIndex=0;dataSourceIndex<dataSourceAmount;dataSourceIndex++){
            //构建歌单
           SongList candidate=SongList.builder()
                    .name(dataSources.get(dataSourceIndex).toString())
                    .songList(dataSourceGroup.get(dataSources.get(dataSourceIndex)))
                    .build();
            songLists.add(candidate);
        }
        return songLists;
    }
}
