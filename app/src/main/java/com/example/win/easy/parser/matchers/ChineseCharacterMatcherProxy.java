package com.example.win.easy.parser.matchers;

import com.example.win.easy.parser.interfaces.MatcherProxy;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.Module;

/**
 * 提供了从字符串中提取中文字并转化为拼音首字母的功能<br/>
 * 如输入"那一夜有没有说(2017-Live)"，输出为一个有序的{@code Map}:
 * <span>
 *     <li>0:N</li>
 *     <li>1:Y</li>
 *     <li>2:Y</li>
 *     <li>3:Y</li>
 *     <li>4:M</li>
 *     <li>5:Y</li>
 *     <li>6:S</li>
 * </span>
 */
@Module
public class ChineseCharacterMatcherProxy implements MatcherProxy<Character> {

    //对中文字的正则表达式
    static final String chineseCharacterRegex="[\\u2E80-\\u9FFF]";
    static final Pattern pattern=Pattern.compile(chineseCharacterRegex);

    private Matcher matcher;
    private SortedMap<Integer,Character> elements=new TreeMap<>();

    public ChineseCharacterMatcherProxy(){
        this("");
    }
    public ChineseCharacterMatcherProxy(String principal){
        reset(principal);
    }

    /**
     * 从主体字符串中提取中文字、转化为首字母大写字符，并将结果存入{@code elements}中
     * @return
     */
    @Override
    public SortedMap<Integer, Character> extract() {
        char element;
        String[] result;

        while (matcher.find()){
            element=matcher.group().charAt(0);//匹配到的中文字符
            result=PinyinHelper.toHanyuPinyinStringArray(element);//转化为读音组
            if (result!=null)
                elements.put(
                        matcher.start(),//字符的位置
                        Character.toUpperCase(result[0].charAt(0))//第一个读音的第一个字符的大写形式
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
