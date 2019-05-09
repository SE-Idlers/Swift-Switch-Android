package com.example.win.easy.repository.web.download;

public class SongDownloadManager extends DownloadManager{
    private static SongDownloadManager instance=new SongDownloadManager();
    public static SongDownloadManager getInstance(){return instance;}
    private SongDownloadManager(){
        super();
    }
}
