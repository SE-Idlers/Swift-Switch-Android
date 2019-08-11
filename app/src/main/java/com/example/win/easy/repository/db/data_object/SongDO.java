package com.example.win.easy.repository.db.data_object;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.enumeration.DataSource;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDO {

    /**
     * 自动生成的歌曲id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌曲名字，用于展示给用户
     * 如“陈奕迅 - 烟味”
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
    public String songPath;

    /**
     * 歌曲图片的绝对路径
     */
    public String avatarPath;

    /**
     * 网络歌曲的用户id
     */
    public String uid;

    /**
     * 网络歌曲的id
     */
    public String remoteId;

}
