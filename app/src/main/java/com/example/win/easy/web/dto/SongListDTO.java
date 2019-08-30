package com.example.win.easy.web.dto;

import com.example.win.easy.enumeration.DataSource;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
@Data
@AllArgsConstructor
public class SongListDTO implements Serializable {

    private static final long serialVersionUID=4L;

    /**
     * 歌单的名字
     */
    public String name;

    /**
     * 歌单包含的歌曲
     */
    @Singular
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


}
