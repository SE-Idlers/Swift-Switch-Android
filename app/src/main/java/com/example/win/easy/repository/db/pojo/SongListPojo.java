package com.example.win.easy.repository.db.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Entity
@Data
public class SongListPojo {

    /**
     * 自动生成的歌单id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌单的名字
     */
    public String name;

}
