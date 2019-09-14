package com.example.win.easy.repository.db.data_object;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.win.easy.enumeration.DataSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongListDO {

    /**
     * 自动生成的歌单id
     */
    @PrimaryKey(autoGenerate = true)
    public Long id;

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
     * 歌单头像的下载url
     */
    public String avatarUrl;

    /**
     * 歌单图片的绝对路径
     */
    public String avatarPath;

    /**
     * 网络歌单的用户id
     */
    public Long uid;

    /**
     * 网络歌单本身的id
     */
    public Long remoteId;
}
