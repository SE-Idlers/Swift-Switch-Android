package com.example.win.easy.repository.task;

import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.domain.NetworkSong;

import java.io.File;

import lombok.Builder;

@Builder
public class SongDownloadTask extends DownloadTask {

    private SongPojoDao songPojoDao;

    private NetworkSong networkSong;
    private long songId;
    private String tempName;
    private String finishName;
    private File tempFile;
    private File finishFile;

    @Override
    public void run() {
        SongPojo songPojo=songPojoDao.findDataById(songId);
        if (songPojo!=null){
            if (songPojo.songPath!=null){
                if (finishFile.exists())
                    return;
                else {
                    songPojo.setSongPath(null);
                    songPojoDao.update(songPojo);
                }
            }else {
                if (finishFile.exists()){
                    songPojo.setSongPath(finishName);
                    songPojoDao.update(songPojo);
                    return;
                }
            }
            if (tempFile.exists())
                tempFile.delete();
            if (download(networkSong.songUrl,tempName)){
                tempFile.renameTo(finishFile);
                songPojo.setSongPath(finishName);
                songPojoDao.update(songPojo);
            }
        }
    }
}
