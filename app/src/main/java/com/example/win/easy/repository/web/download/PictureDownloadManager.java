package com.example.win.easy.repository.web.download;

public class PictureDownloadManager extends DownloadManager {

    private static PictureDownloadManager instance=new PictureDownloadManager();
    public static PictureDownloadManager getInstance(){return instance;}
    private PictureDownloadManager(){
        super();
    }
}
