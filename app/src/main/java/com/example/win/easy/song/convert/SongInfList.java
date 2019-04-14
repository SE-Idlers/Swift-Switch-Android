package com.example.win.easy.song.convert;

import com.example.win.easy.song.Song;
import com.example.win.easy.song.convert.parser.RegulationFilenameParser;
import com.example.win.easy.song.convert.parser.interfaces.FilenameParser;

import java.io.File;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 这是提取的歌曲信息的总列表
 * 实现了文件到歌曲列表的转化
 * 我觉得用不到remove啊
 * */

public class SongInfList implements File2SongConverter {

    public SongInfList() {
        //default constructor
    }

    public  Song convert(File file) {//得到歌曲全列表

            //delete prefix and postfix
            Song tmp = new Song();
            tmp.setName(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('-'), file.getAbsolutePath().indexOf('.')));
            tmp.setAuthor(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('c') + 2, file.getAbsolutePath().indexOf('-')));
            //得到关键码,
            //the datatype should be modified, and an instance needed here
            FilenameParser instance = new RegulationFilenameParser();
            tmp.setSequence(instance.parse(tmp.getName()));
            return tmp;
    }
}
