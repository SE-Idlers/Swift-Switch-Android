package com.example.win.easy.repository.db.data_object;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.repository.web.dto.SongListDTO;
import com.example.win.easy.enumeration.DataSource;

import lombok.Data;

@Entity
@Data
public class SongListDO {

    /**
     * 自动生成的歌单id
     */
    @PrimaryKey(autoGenerate = true)
    public long id;

    /**
     * 歌单的名字
     */
    public String name;

    /**
     * 歌单来源
     * @see DataSource
     */
    public DataSource source;

    /**
     * 歌单图片的绝对路径
     */
    public String avatarPath;

    /**
     * 网络歌单的用户id
     */
    public String uid;

    /**
     * 网络歌单本身的id
     */
    public String remoteId;

    public SongListDO(){}
    public SongListDO(SongListDTO songListDTO){
        this.name= songListDTO.name;
        this.source= songListDTO.source;
        this.uid= songListDTO.uid;
        this.remoteId= songListDTO.remoteId;
    }
    public SongListDO(DataSource dataSource){
        this.source=dataSource;
        this.name=source.toString();
    }
}
