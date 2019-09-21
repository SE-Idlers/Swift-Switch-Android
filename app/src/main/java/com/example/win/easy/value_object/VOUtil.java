package com.example.win.easy.value_object;

import androidx.lifecycle.LiveData;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;

import java.util.List;

/**
 * <p>职责和功能类似{@link com.example.win.easy.web.DTOUtil}</p>
 * @see com.example.win.easy.web.DTOUtil
 */
public class VOUtil {


    public SongDO toDO(SongVO songVO){
        return SongDO.builder()
                .id(songVO.getId())
                .name(songVO.getName())
                .songUrl(songVO.getSongUrl())
                .songPath(songVO.getSongPath())
                .avatarUrl(songVO.getAvatarUrl())
                .avatarPath(songVO.getAvatarPath())
                .source(songVO.getSource())
                .build();
    }

    public SongVO toVO(SongDO songDO){
        return SongVO.builder()
                .id(songDO.getId())
                .name(songDO.getName())
                .songUrl(songDO.getSongUrl())
                .songPath(songDO.getSongPath())
                .avatarUrl(songDO.getAvatarUrl())
                .avatarPath(songDO.getAvatarPath())
                .source(songDO.getSource())
                .build();
    }

    public SongListVO toVO(SongListDO songListDO){
        return SongListVO.builder()
                .id(songListDO.getId())
                .name(songListDO.getName())
                .avatarUrl(songListDO.getAvatarUrl())
                .avatarPath(songListDO.getAvatarPath())
                .dataSource(songListDO.getSource())
                .build();
    }

    public SongListDO toDO(SongListVO songListVO){
        return SongListDO.builder()
                .id(songListVO.getId())
                .name(songListVO.getName())
                .avatarUrl(songListVO.getAvatarUrl())
                .avatarPath(songListVO.getAvatarPath())
                .source(songListVO.getDataSource())
                .build();
    }

}
