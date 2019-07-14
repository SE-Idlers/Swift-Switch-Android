package com.example.win.easy.repository.web;

import com.example.win.easy.repository.web.domain.NetworkSong;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class DownloadFilenameResolver {

    private final String rootDir;
    private final String suffixOfTemp;

    @Inject
    public DownloadFilenameResolver(
            @Named("downloadRootDir") String downloadRootDir,
            @Named("suffixOfTempFile") String suffixOfTempFile){
        this.rootDir=downloadRootDir;
        this.suffixOfTemp=suffixOfTempFile;
    }

    public String tempSongFilePath(NetworkSong networkSong){
        return finishSongFilePath(networkSong)+ suffixOfTemp;
    }

    public String finishSongFilePath(NetworkSong networkSong){
        return root()+user(networkSong)+category(FileType.Song)+filename(networkSong,FileType.Song);
    }

    public String tempPictureFilePath(NetworkSong networkSong){
        return finishPictureFilePath(networkSong)+tempSuffix();
    }

    public String finishPictureFilePath(NetworkSong networkSong){
        return root()+user(networkSong)+category(FileType.Picture)+filename(networkSong,FileType.Picture);
    }
    public String tempSuffix(){
        return suffixOfTemp;
    }

    public String root(){
        return rootDir;
    }


    private String user(NetworkSong networkSong){
        return "/"+networkSong.uid;
    }

    private String category(FileType fileType){
        return "/"+fileType.toString();
    }
    private String filename(NetworkSong networkSong,FileType fileType){
        switch (fileType){
            case Song:
                return "/"+networkSong.totalName+networkSong.extensionName;
            case Picture:
                return "/"+networkSong.totalName+".jpg";
            default:
                return null;
        }
    }

    private enum FileType{
        Song,
        Picture
    }
}
