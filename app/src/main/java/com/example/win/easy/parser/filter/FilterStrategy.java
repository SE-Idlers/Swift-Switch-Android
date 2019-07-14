package com.example.win.easy.parser.filter;

import java.util.List;

/**
 * 根据依据{@code basis}对输入待过滤列表进行过滤<br/>
 * 总体逻辑大致如下:<img src="Cooperation-Logic.png" height="800" width="1200" />
 * @param <T> 表示一个过滤依据<br/>
 * @see CharSequenceFilterStrategy <br/>
 */
public interface FilterStrategy<T> {

    /**
     * 具体功能可见{@link CharSequenceFilterStrategy}
     * @param basis 过滤依据依据，如字符序列
     * @param listToFilter 待过滤的列表
     * @return 成功通过过滤的序列（candidates）下标
     */
    List<Integer> filter(T basis,List<T> listToFilter);

}
