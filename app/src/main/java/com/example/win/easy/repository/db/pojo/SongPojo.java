package com.example.win.easy.repository.db.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.repository.web.domain.NetworkSong;
import com.example.win.easy.song.DataSource;
import com.example.win.easy.song.convert.parser.RegulationFilenameParser;

import java.util.List;

import lombok.Data;

@Entity
@Data
public class SongPojo {

    /**
     * 自动生成的歌曲id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌曲名字，用于展示给用户
     * 如“陈奕迅 - 烟味”
     */
    public String name;

    /**
     * 歌曲作者
     */
    public String author;

    /**
     * 歌曲的识别序列
     */
    public List<Character> sequence;

    /**
     * 歌曲来源
     * @see DataSource
     */
    public DataSource source;

    /**
     * 歌曲的绝对路径
     */
    public String songPath;

    /**
     * 歌曲图片的绝对路径
     */
    public String avatarPath;

    /**
     * 网络歌曲的用户id
     */
    public String uid;

    /**
     * 网络歌曲的id
     */
    public String remoteId;

    public SongPojo(){}
    public SongPojo(NetworkSong networkSong){
        this.name=networkSong.totalName;
        this.author=networkSong.author;
        this.source=networkSong.source;
        this.uid=networkSong.uid;
        this.remoteId=networkSong.remoteId;
        this.sequence= RegulationFilenameParser.getInstance().parse(networkSong.name);
    }

}
