package com.example.win.easy.repository.db.data_object

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.win.easy.enumeration.DataSource
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.io.Serializable

@Entity
data class SongListDO(
        /**
     * 自动生成的歌单id
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long?=null,

        /**
     * 歌单的名字
     */
    var name: String?=null,

        /**
     * 歌单来源
     * @see DataSource
     */
    var source: DataSource?=null,

        /**
     * 歌单头像的下载url
     */
    var avatarUrl: String?=null,

        /**
     * 歌单图片的绝对路径
     */
    var avatarPath: String?=null,

        /**
     * 网络歌单的用户id
     */
    var uid: Long?=null,

        /**
     * 网络歌单本身的id
     */
    var remoteId: Long?=null
): Serializable