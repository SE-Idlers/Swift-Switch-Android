package com.example.win.easy.repository.web.domain;

import com.example.win.easy.song.DataSource;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

//@Builder
@Data
public class NetworkSongList implements Serializable {

    private static final long serialVersionUID=4L;

    /**
     * 歌单的名字
     */
    public String name;

    /**
     * 歌单包含的歌曲
     */
    public List<NetworkSong> networkSongs;

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

    public NetworkSongList(LinkedTreeMap<String,Object> treeMap){
        this.name=(String) treeMap.get("name");
        List<NetworkSong> networkSongs=new ArrayList<>();
        for (LinkedTreeMap<String,Object> _treeMap:(List<LinkedTreeMap<String,Object>>)treeMap.get("networkSongs"))
            networkSongs.add(new NetworkSong(_treeMap));
        this.networkSongs=networkSongs;
        this.avatarUrl=(String)treeMap.get("avatarUrl");
        this.source=DataSource.valueOf((String) treeMap.get("source"));
        this.uid=(String)treeMap.get("uid");
        this.remoteId=(String)treeMap.get("remoteId");
    }

}
