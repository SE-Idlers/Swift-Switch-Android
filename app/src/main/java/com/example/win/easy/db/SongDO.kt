package com.example.win.easy.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.win.easy.enumeration.DataSource

@Entity
data class SongDO(
        /**
     * 自动生成的歌曲id
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long?=null,
        /**
     * 歌曲名字，用于展示给用户
     * 如“陈奕迅 - 烟味”
     */
    var name: String="",

        /**
     * 歌曲作者
     */
    var author: String?=null,

        /**
     * 歌曲的识别序列
     */
    var sequence: List<Char>?=null,

        /**
     * 歌曲来源
     * @see DataSource
     */
    var source: DataSource=DataSource.Local,

        /**
     * 歌曲的下载url
     */
    var songUrl: String?=null,

        /**
     * 歌曲的绝对路径
     */
    var songPath: String?=null,

        /**
     * 歌曲头像的下载url
     */
    var avatarUrl: String?=null,

        /**
     * 歌曲图片的绝对路径
     */
    var avatarPath: String?=null,

        /**
     * 网络歌曲的用户id
     */
    var uid: Long?=null,

        /**
     * 网络歌曲的id
     */
    var remoteId: Long?=null
){
    override fun equals(other: Any?): Boolean =
            if (other !is SongDO || source!= other.source)
                false
            else
                when(source){
                    DataSource.Local -> super.equals(other)
                    else -> uid==other.uid && remoteId==other.remoteId
                }
}
