package com.example.win.easy.filter;

import java.io.File;
import java.util.List;

/**
 * 将单个文件名转化为一个结果序列，用于过滤
 * @param <T> 与手写板识别结果一致的类型,参考{@link FilterStrategy}<br/>
 * 协作逻辑如: <img src="logic.png" height="800" width="1200" />
 */
public interface FilenameParser<T> {

    /**
     * @param file 输入的文件
     * @return 转化后得到的结果序列
     */
    List<T>  parse(File file);
}
