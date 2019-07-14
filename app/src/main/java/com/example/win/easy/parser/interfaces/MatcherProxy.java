package com.example.win.easy.parser.interfaces;

import com.example.win.easy.parser.filter.FilterStrategy;
import com.example.win.easy.parser.matchers.WordMatcherProxy;

import java.util.SortedMap;

/**
 * 尝试对从一个字符串中提取某一类语素信息，并将每一个语素转化为一个{@code T}类型的对象，
 * 注意不同的实现类各自能够提取的语素类型之间不应该有交集，具体实现类如{@link WordMatcherProxy},逻辑图如下：<br/>
 * <img src="MatcherProxy-Logic.png" height="600" width="800"/>
 * @see WordMatcherProxy
 * @param <T> 一个单词对应的表示结果类型，对应于{@link FilterStrategy}<br/>
 */
public interface MatcherProxy<T> {

    /**
     * 从一个字符串中提取某一类型的语素
     * @return 一个有序的表，每一个{@code pair}对应一个找到的语素，{@code key}表示第一次出现的下标，{@code value}表示该语素转化成的{@code T}对象
     */
    SortedMap<Integer,T> extract();

    /**
     * 重新设置主体字符串，同时重置结果等，取决于具体的实现逻辑
     * @param principal 新的主体字符串
     */
    void reset(String principal);

}
