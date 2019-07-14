package com.example.win.easy.repository.task;

import com.example.win.easy.application.SwiftSwitchApplication;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.DownloadFilenameResolver;
import com.example.win.easy.repository.web.domain.NetworkSong;

import java.io.File;

public class SongPictureDownloadTask extends DownloadTask {

    private NetworkSong networkSong;
    private long songId;
    private String tempName;
    private String finishName;
    private File tempFile;
    private File finishFile;
    private SongPojoDao songPojoDao;

    public SongPictureDownloadTask(NetworkSong networkSong,long songId){
        this.networkSong=networkSong;
        this.songId=songId;
        this.tempName= DownloadFilenameResolver.tempPictureFilePath(networkSong);
        this.finishName= DownloadFilenameResolver.finishPictureFilePath(networkSong);
        this.tempFile=new File(tempName);
        this.finishFile=new File(finishName);
        this.songPojoDao= SwiftSwitchApplication.application.getAppComponent().getSongPojoDao();
    }

    @Override
    public void run() {
        SongPojo songPojo=songPojoDao.findDataById(songId);
        if (songPojo!=null){
//            //TODO 下载流逻辑
//
//
//            if (download(networkSong.songUrl,tempName)){
//                tempFile.renameTo(finishFile);
//                songPojo.setSongPath(finishName);
//                songPojoDao.updateToSelectingSong(songPojo);
//            }
        }
    }
}
