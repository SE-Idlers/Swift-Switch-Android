package com.example.win.easy.parser.matchers;

import com.example.win.easy.parser.interfaces.MatcherProxy;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.Module;

/**
 * 提供了从一个字符串中提取一系列数字串的功能，同时把每一个独立的数字串的第一个数字用来表示这个数字串<br/>
 * 如对输入"编号89757-(2018Live)第8版",输出结果为:
 * <span>
 *     <li>2:8</li>
 *     <li>9:2</li>
 *     <li>19:8</li>
 * </span>
 */
@Module
public class NumberMatcherProxy implements MatcherProxy<Character> {

    //匹配数字串的正则表达式
    static final String numberStringRegex="\\d+";
    static final Pattern pattern=Pattern.compile(numberStringRegex);

    private Matcher matcher;
    private SortedMap<Integer,Character> elements=new TreeMap<>();

    public NumberMatcherProxy(){this("");}
    public NumberMatcherProxy(String principal){
        reset(principal);
    }

    @Override
    public SortedMap<Integer, Character> extract() {
        while (matcher.find()){
            elements.put(
                    matcher.start(),//数字串的起始位置
                    matcher.group().charAt(0)//取第一个数字作为表示该数字串的字符
            );
        }
        return elements;
    }

    /**
     * 重置proxy，清空之前的结果
     * @see MatcherProxy#reset(String)
     * @param principal 新的主体字符串
     */
    @Override
    public void reset(String principal) {
        matcher=pattern.matcher(principal);
        elements.clear();
    }
}
