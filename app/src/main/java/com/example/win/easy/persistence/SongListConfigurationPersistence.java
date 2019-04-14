package com.example.win.easy.persistence;

import com.example.win.easy.display.SongList;

import java.util.List;

//阿里巴巴的fastjson框架

public class SongListConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<List<SongList>> {//歌单
    static String fileDir="/SwiftSwitch/src/SongListConfig.json";//SD中的存储地址

    public SongListConfigurationPersistence(){
        super(fileDir);
    }
}
