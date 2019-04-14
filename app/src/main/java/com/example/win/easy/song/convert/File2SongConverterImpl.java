package com.example.win.easy.song.convert;

import com.example.win.easy.song.Song;
import com.example.win.easy.song.convert.parser.RegulationFilenameParser;
import com.example.win.easy.song.convert.parser.interfaces.FilenameParser;

import java.io.File;

/**
 * 这是提取的歌曲信息的总列表
 * 实现了文件到歌曲列表的转化
 * 我觉得用不到remove啊
 * */

public class File2SongConverterImpl implements File2SongConverter {

    private static FilenameParser filenameParser=RegulationFilenameParser.getInstance();
    private static File2SongConverterImpl instance=new File2SongConverterImpl();
    public static File2SongConverterImpl getInstance(){return instance;}
    private File2SongConverterImpl(){}

    /**
     * 将歌曲文件转化为歌曲对象
     * @param file 输入的歌曲文件
     * @return 转化后得到的歌曲
     */
    public  Song convert(File file) {

            //delete prefix and postfix
            Song song = new Song();
            song.setName(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('-'), file.getAbsolutePath().indexOf('.')));
            song.setAuthor(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('c') + 2, file.getAbsolutePath().indexOf('-')));

            //得到关键码
            song.setSequence(filenameParser.parse(song.getName()));
            return song;
    }
}
