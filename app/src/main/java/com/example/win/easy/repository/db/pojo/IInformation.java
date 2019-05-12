package com.example.win.easy.repository.db.pojo;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

/**
 * 歌曲的播放信息，与歌曲是一对一的关系
 */
@Entity(
        foreignKeys = {
                @ForeignKey(
                        entity = SongPojo.class,
                        parentColumns = "id",
                        childColumns = "songId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("songId")
        }
)
@Data
public class IInformation {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 指向具体某首歌曲
     */
    public long songId;

    /**
     * 最近一次被播放时间
     */
    public Date mostRecentlyDisplayedDate;

}
