package com.example.win.easy.repository.db.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.repository.web.domain.NetworkSongList;
import com.example.win.easy.song.DataSource;

import lombok.Data;

@Entity
@Data
public class SongListPojo {

    /**
     * 自动生成的歌单id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌单的名字
     */
    public String name;

    /**
     * 歌单来源
     * @see DataSource
     */
    public DataSource source;

    /**
     * 歌单图片的绝对路径
     */
    public String avatarPath;

    /**
     * 网络歌单的用户id
     */
    public String uid;

    /**
     * 网络歌单本身的id
     */
    public String remoteId;

    public SongListPojo(){}
    public SongListPojo(NetworkSongList networkSongList){
        this.name=networkSongList.name;
        this.source=networkSongList.source;
        this.uid=networkSongList.uid;
        this.remoteId=networkSongList.remoteId;
    }
}
