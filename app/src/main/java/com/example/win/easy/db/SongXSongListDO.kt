package com.example.win.easy.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.win.easy.db.SongDO
import com.example.win.easy.db.SongListDO

/**
 * 用于表示Song与SongList多对多关系的表
 */
@Entity(
        foreignKeys = [
            ForeignKey(entity = SongDO::class, parentColumns = ["id"], childColumns = ["songId"], onDelete = ForeignKey.CASCADE),
            ForeignKey(entity = SongListDO::class, parentColumns = ["id"], childColumns = ["songListId"], onDelete = ForeignKey.CASCADE)],
        primaryKeys = ["songId", "songListId"],
        indices = [Index("songId"), Index("songListId")])
data class SongXSongListDO(
    /**
     * ManyToMany关系的Song的id
     */
    val songId: Long,

    /**
     * ManyToMany关系的SongList的id
     */
    var songListId: Long
)