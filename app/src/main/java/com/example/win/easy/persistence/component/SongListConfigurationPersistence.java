package com.example.win.easy.persistence.component;

import com.example.win.easy.display.SongList;
import com.example.win.easy.persistence.interfaces.AbstractJsonifyConfigurationPersistence;

import java.util.ArrayList;
import java.util.List;

//阿里巴巴的fastjson框架

public class SongListConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<List<SongList>> {//歌单

    private static String fileDir="/SwiftSwitch/src/SongListConfig.json";//SD中的存储地址
    private static SongListConfigurationPersistence instance=new SongListConfigurationPersistence();
    public static SongListConfigurationPersistence getInstance(){return instance;}
    private SongListConfigurationPersistence(){
        super(fileDir);
    }

    @Override
    protected Class<List<SongList>> getClassInformation() {
        List<SongList> tempSongList=new ArrayList<>();
        return (Class<List<SongList>>) tempSongList.getClass();
    }
}
