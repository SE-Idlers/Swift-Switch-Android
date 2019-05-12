package com.example.win.easy.repository.web;

import android.os.Environment;

import com.example.win.easy.repository.web.domain.NetworkSong;

public class DownloadFilenameResolver {

    private static final String root= Environment.getExternalStorageDirectory().getAbsolutePath()+"/Swift Switch";
    private static final String tempSuffix="~TEMP";

    public static String tempSongFilePath(NetworkSong networkSong){
        return finishSongFilePath(networkSong)+tempSuffix;
    }

    public static String finishSongFilePath(NetworkSong networkSong){
        return root()+user(networkSong)+category(FileType.Song)+filename(networkSong,FileType.Song);
    }

    public static String tempPictureFilePath(NetworkSong networkSong){
        return finishPictureFilePath(networkSong)+tempSuffix();
    }

    public static String finishPictureFilePath(NetworkSong networkSong){
        return root()+user(networkSong)+category(FileType.Picture)+filename(networkSong,FileType.Picture);
    }
    public static String tempSuffix(){
        return tempSuffix;
    }

    public static String root(){
        return root;
    }


    private static String user(NetworkSong networkSong){
        return "/"+networkSong.uid;
    }

    private static String category(FileType fileType){
        return "/"+fileType.toString();
    }
    private static String filename(NetworkSong networkSong,FileType fileType){
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
