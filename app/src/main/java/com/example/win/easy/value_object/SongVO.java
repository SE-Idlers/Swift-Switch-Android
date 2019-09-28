package com.example.win.easy.value_object;


import androidx.room.PrimaryKey;

import com.example.win.easy.enumeration.DataSource;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongVO {

    /**
     * 自动生成的歌曲id
     */
    @PrimaryKey(autoGenerate = true)
    private Long id;

    public Long getId(){
        return id;
    }
    /**
     * 歌曲名字，用于展示给用户
     * 如“陈奕迅 - 烟味”
     */
    private String name;

    public String getName(){
        return name;
    }

    /**
     * 歌曲作者
     */
    private String author;

    public String getAuthor(){
        return author;
    }

    private String songPath;

    /**
     * 歌曲的绝对路径
     */
    public String getSongPath() {
        return songPath;
    }
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
     * 歌曲的下载url
     */
    private String songUrl;

    public String getSongUrl(){
        return songUrl;
    }


    /**
     * 歌曲头像的下载url
     */
    private String avatarUrl;

    public String getAvatarUrl(){
        return avatarUrl;
    }


    /**
     * 歌曲图片的绝对路径
     */
    public String avatarPath;

    public String getAvatarPath(){
        return avatarPath;
    }
    /**
     * 网络歌曲的用户id
     */
    public Long uid;

    /**
     * 网络歌曲的id
     */
    public Long remoteId;


    public boolean songFileHasBeenDownloaded(){return songPath!=null;}
    public boolean songFileCanBeDownloaded(){return songUrl!=null;}

}
