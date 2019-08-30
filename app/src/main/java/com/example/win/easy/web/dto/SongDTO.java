package com.example.win.easy.web.dto;

import com.example.win.easy.enumeration.DataSource;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO implements Serializable {

    private static final long serialVersionUID=5L;

    /**
     * 歌曲名字，用于给用户看<br/>
     * 如“陈奕迅 - 烟味”
     */
    public String totalName;

    /**
     * 仅歌曲名字<br/>
     * 如“烟味”
     */
    public String name;

    /**
     * 歌曲作者
     */
    public String author;

    /**
     * 文件扩展名<br/>
     * 如".mp3"
     */
    public String extensionName;

    /**
     * 歌曲的下载url
     */
    public String songUrl;

    /**
     * 歌曲图片的下载Url
     */
    public String avatarUrl;

    /**
     * 歌曲来源
     */
    public DataSource source;

    /**
     * 网络歌曲的用户id
     */
    public String uid;

    /**
     * 网络歌曲本身的id
     */
    public String remoteId;

}
