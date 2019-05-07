package com.example.win.easy.repository.db.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.song.DataSource;

import java.util.List;

import lombok.Data;

@Entity
@Data
public class SongPojo {

    /**
     * 自动生成的歌曲id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌曲名字，用于展示给用户
     */
    public String name;

    /**
     * 歌曲作者
     */
    public String author;

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
     * 歌曲的绝对路径
     */
    public String absolutePath;

}
