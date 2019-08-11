package com.example.win.easy.repository.task;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.web.dto.SongDTO;

import java.io.File;

import lombok.Builder;

@Builder
public class SongDownloadTask extends DownloadTask {

    private SongDao songDao;

    private SongDTO songDTO;
    private long songId;
    private String tempName;
    private String finishName;
    private File tempFile;
    private File finishFile;

    @Override
    public void run() {
        SongDO songDO = songDao.findDataById(songId);
        if (songDO !=null){
            if (songDO.songPath!=null){
                if (finishFile.exists())
                    return;
                else {
                    songDO.setSongPath(null);
                    songDao.update(songDO);
                }
            }else {
                if (finishFile.exists()){
                    songDO.setSongPath(finishName);
                    songDao.update(songDO);
                    return;
                }
            }
            if (tempFile.exists())
                tempFile.delete();
            if (download(songDTO.songUrl,tempName)){
                tempFile.renameTo(finishFile);
                songDO.setSongPath(finishName);
                songDao.update(songDO);
            }
        }
    }
}
