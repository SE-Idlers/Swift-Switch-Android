package com.example.win.easy.persistence.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.win.easy.persistence.interfaces.AbstractJsonifyConfigurationPersistence;
import com.example.win.easy.songList.SongList;

import java.util.ArrayList;
import java.util.Arrays;
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
    protected String toJsonString(List<SongList> entity) {
        return JSON.toJSONString(entity.toArray());
    }

    @Override
    protected List<SongList> fromJsonString(String json) {
        return JSON.parseArray(json, SongList.class);//含数组的String转为JSONArray
    }

    @Override
    protected void writeEmptyObject() {
        save(new ArrayList<>(Arrays.asList(new SongList[]{})));
    }

    @Override
    protected List<SongList> getEmptyInstance() {
        return new ArrayList<>();
    }
}
