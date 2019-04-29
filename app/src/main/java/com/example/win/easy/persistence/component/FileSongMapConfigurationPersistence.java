package com.example.win.easy.persistence.component;

import com.alibaba.fastjson.JSON;
import com.example.win.easy.persistence.interfaces.AbstractJsonifyConfigurationPersistence;
import com.example.win.easy.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//阿里巴巴的fastjson框架


public class FileSongMapConfigurationPersistence extends AbstractJsonifyConfigurationPersistence<Map<File,Song>> {

    private static String fileDir="/SwiftSwitch/src/MapOfFileAndSong.json";
    private static FileSongMapConfigurationPersistence instance=new FileSongMapConfigurationPersistence();
    public static FileSongMapConfigurationPersistence getInstance() { return  instance; }//线程安全
    private FileSongMapConfigurationPersistence(){
        super(fileDir);
    }

    @Override
    protected String toJsonString(Map<File, Song> entity) {
        List<File> fileList=new ArrayList<>(entity.keySet());
        int fileAmount=fileList.size();
        FileSong[] fileSongs=new FileSong[fileList.size()];
        for (int fileIndex=0;fileIndex<fileAmount;fileIndex++)
            fileSongs[fileIndex]=FileSong.builder()
                    .file(fileList.get(fileIndex))
                    .song(entity.get(fileList.get(fileIndex)))
                    .build();
        return JSON.toJSONString(fileSongs);
    }

    @Override
    protected Map<File, Song> fromJsonString(String json) {
        List<FileSong> shadow=JSON.parseArray(json,FileSong.class);//含数组的String转为JSONArray
        Map<File,Song> map=new HashMap<>();
        for(FileSong fileSong:shadow)
            map.put(fileSong.getFile(),fileSong.getSong());
        return map;
    }

    @Override
    protected void writeEmptyObject() {
        save(new HashMap<File, Song>());
    }

    @Override
    protected Map<File, Song> getEmptyInstance() {
        return new HashMap<>();
    }
}
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FileSong{
    private File file;
    private Song song;
}
