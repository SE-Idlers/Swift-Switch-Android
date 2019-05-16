package com.example.win.easy.repository.web.domain;

import com.example.win.easy.song.DataSource;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
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

}
