package com.example.win.easy.repository.web.download;

import com.example.win.easy.repository.web.dto.SongDTO;

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

    public String tempSongFilePath(SongDTO songDTO){
        return finishSongFilePath(songDTO)+ suffixOfTemp;
    }

    public String finishSongFilePath(SongDTO songDTO){
        return root()+user(songDTO)+category(FileType.Song)+filename(songDTO,FileType.Song);
    }

    public String tempPictureFilePath(SongDTO songDTO){
        return finishPictureFilePath(songDTO)+tempSuffix();
    }

    public String finishPictureFilePath(SongDTO songDTO){
        return root()+user(songDTO)+category(FileType.Picture)+filename(songDTO,FileType.Picture);
    }
    public String tempSuffix(){
        return suffixOfTemp;
    }

    public String root(){
        return rootDir;
    }


    private String user(SongDTO songDTO){
        return "/"+ songDTO.uid;
    }

    private String category(FileType fileType){
        return "/"+fileType.toString();
    }
    private String filename(SongDTO songDTO, FileType fileType){
        switch (fileType){
            case Song:
                return "/"+ songDTO.totalName+ songDTO.extensionName;
            case Picture:
                return "/"+ songDTO.totalName+".jpg";
            default:
                return null;
        }
    }

    private enum FileType{
        Song,
        Picture
    }
}
