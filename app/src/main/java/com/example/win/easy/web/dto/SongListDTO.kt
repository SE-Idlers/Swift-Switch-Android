package com.example.win.easy.web.dto

import com.example.win.easy.enumeration.DataSource
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import java.io.Serializable

class SongListDTO(
    /**
     * 歌单的名字
     */
    var name: String? = null,

    /**
     * 歌单图片下载Url
     */
    var avatarUrl: String? = null,

    /**
     * 歌单的来源
     */
    var source: DataSource? = null,

    /**
     * 歌单的用户id
     */
    var uid: String,

    /**
     * 歌单自身的id
     */
    var remoteId: String

): Serializable