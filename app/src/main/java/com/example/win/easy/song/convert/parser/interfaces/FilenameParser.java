package com.example.win.easy.song.convert.parser.interfaces;

import com.example.win.easy.filter.FilterStrategy;

import java.util.List;

/**
 * 将单个文件名转化为一个结果序列，这个结果序列用来表述这个文件
 * @param <T> 与手写板识别结果一致的类型,参考{@link FilterStrategy}<br/>
 * 协作逻辑如: <img src="Parser-Logic.png" height="700" width="900" />
 */
public interface FilenameParser<T> {

    /**
     * @param filename 输入的文件名
     * @return 转化后得到的结果序列
     */
    List<T> parse(String filename);
}
