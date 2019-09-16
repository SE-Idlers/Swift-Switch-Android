package com.example.win.easy.value_object;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongVO {

    Long id;

    String name;

    String songFileUrl;

    String songFilePath;

    String avatarUrl;

    String avatarPath;


    public boolean songFileHasBeenDownloaded(){return songFilePath!=null;}
    public boolean songFileCanBeDownloaded(){return songFileUrl!=null;}
}
