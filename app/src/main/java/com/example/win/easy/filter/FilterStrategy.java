package com.example.win.easy.filter;

import java.util.List;

/**
 * 根据依据{@code basis}对输入序列列表进行过滤<br/>
 * @param <T> 表示一个手写板的识别结果类型<br/>
 * @see CharSequenceFilterStrategy <br/>
 */
public interface FilterStrategy<T> {

    /**
     * @param basis 有序的一系列依据，如字符序列
     * @param sequenceList 待过滤的序列列表
     * @return 成功通过过滤的序列（candidates）下标
     */
    List<Integer> filter(List<T> basis,List<List<T>> sequenceList);

}
