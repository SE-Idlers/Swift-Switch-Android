package com.example.win.easy.repository.task;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.web.dto.SongDTO;

import java.io.File;

import lombok.Builder;

@Builder
public class SongPictureDownloadTask extends DownloadTask {

    private SongDTO songDTO;
    private long songId;
    private String tempName;
    private String finishName;
    private File tempFile;
    private File finishFile;
    private SongDao songDao;

    @Override
    public void run() {
        SongDO songDO = songDao.findDataById(songId);
        if (songDO !=null){
//            //TODO 下载流逻辑
//
//
//            if (download(songDTO.songUrl,tempName)){
//                tempFile.renameTo(finishFile);
//                songDO.setSongPath(finishName);
//                songDao.updateToSelectingSong(songDO);
//            }
        }
    }
}
