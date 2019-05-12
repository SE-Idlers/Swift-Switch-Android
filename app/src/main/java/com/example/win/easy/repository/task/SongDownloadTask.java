package com.example.win.easy.repository.task;

import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.web.DownloadFilenameResolver;
import com.example.win.easy.repository.web.domain.NetworkSong;

import java.io.File;

public class SongDownloadTask extends DownloadTask {

    private NetworkSong networkSong;
    private long songId;
    private String tempName;
    private String finishName;
    private File tempFile;
    private File finishFile;
    private SongPojoDao songPojoDao;

    public SongDownloadTask(NetworkSong networkSong,long songId){
        this.networkSong=networkSong;
        this.songId=songId;
        this.tempName=DownloadFilenameResolver.tempSongFilePath(networkSong);
        this.finishName= DownloadFilenameResolver.finishSongFilePath(networkSong);
        this.tempFile=new File(tempName);
        this.finishFile=new File(finishName);
        this.songPojoDao= SwiftSwitchClassLoader.getOurDatabase().songPojoDao();
    }

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
