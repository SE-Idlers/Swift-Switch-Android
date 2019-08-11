package com.example.win.easy.repository.web.dto;

import com.example.win.easy.enumeration.DataSource;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

//@Builder
@Data
public class SongListDTO implements Serializable {

    private static final long serialVersionUID=4L;

    /**
     * 歌单的名字
     */
    public String name;

    /**
     * 歌单包含的歌曲
     */
    public List<SongDTO> songDTOs;

    /**
     * 歌单图片下载Url
     */
    public String avatarUrl;

    /**
     * 歌单的来源
     */
    public DataSource source;

    /**
     * 歌单的用户id
     */
    public String uid;

    /**
     * 歌单自身的id
     */
    public String remoteId;

    public SongListDTO(LinkedTreeMap<String,Object> treeMap){
        this.name=(String) treeMap.get("name");
        List<SongDTO> songDTOs =new ArrayList<>();
        for (LinkedTreeMap<String,Object> _treeMap:(List<LinkedTreeMap<String,Object>>)treeMap.get("songDTOs"))
            songDTOs.add(new SongDTO(_treeMap));
        this.songDTOs = songDTOs;
        this.avatarUrl=(String)treeMap.get("avatarUrl");
        this.source=DataSource.valueOf((String) treeMap.get("source"));
        this.uid=(String)treeMap.get("uid");
        this.remoteId=(String)treeMap.get("remoteId");
    }

}
