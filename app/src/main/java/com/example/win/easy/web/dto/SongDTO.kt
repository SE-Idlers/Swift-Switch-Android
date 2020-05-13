package com.example.win.easy.web.dto

import com.example.win.easy.enumeration.DataSource
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SongDTO(
    /**
     * 歌曲名字，用于给用户看<br></br>
     * 如“陈奕迅 - 烟味”
     */
    var totalName: String,

    /**
     * 仅歌曲名字<br></br>
     * 如“烟味”
     */
    var name: String,

    /**
     * 歌曲作者
     */
    var author: String,

    /**
     * 文件扩展名<br></br>
     * 如".mp3"
     */
    var extensionName: String? = null,

    /**
     * 歌曲的下载url
     */
    var songUrl: String,

    /**
     * 歌曲图片的下载Url
     */
    var avatarUrl: String,

    /**
     * 歌曲来源
     */
    var source: DataSource,

    /**
     * 网络歌曲的用户id
     */
    var uid: String,

    /**
     * 网络歌曲本身的id
     */
    var remoteId: String
) : Serializable