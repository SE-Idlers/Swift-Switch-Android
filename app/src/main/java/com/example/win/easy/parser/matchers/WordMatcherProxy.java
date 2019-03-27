package com.example.win.easy.parser.matchers;

import com.example.win.easy.parser.interfaces.MatcherProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.Module;

/**
 * 将单词分为纯小写单词、纯大写单词、大写起头单词三类，提供了对英文单词的提取功能
 */
@Module
public class WordMatcherProxy implements MatcherProxy<Character> {

    static  final String nonWord="[^a-zA-Z]";//非单词字符
    static  final String lowerWord="[a-z]+";//纯小写单词
    static  final String upperWord="[A-Z]+";//纯大写单词
    static  final String normalWord="[A-Z][a-z]+";//大写起头的单词
    static  final String upperLetter="[A-Z]";//大写字母
    static  final String lowerLetter="[a-z]";//小写字母

    /**
     * 用于表示matcher的类型，注意这个类型指的是：
     * 相同类型的matcher在后续处理的时候方法一样，
     * 可能一个matcher既匹配一部分的全大写单词，
     * 也匹配一部分的大写开头单词，之所用归为一类，
     * 是因为他们后续处理方法是一样的
     *
     * 这里的“处理”指的主要是以下两个方面：
     * 1.匹配成功后对匹配词的第一个字母直接返回（因为已经是大写）还是需要先转换成大写
     * 2.成功匹配一次以后matcher指针需要前移的offset大小
     * 在这里，“1”对应的是“L”和“U”前缀，“L”表示需要转换，“U”表示不需要转换
     * “2”对应的是前移offset大小，目前取值有1，2，3，比如“L1”、“U3”分别指
     *  offset为1和3
     */
    static final int L1=0;
    static final int L2=1;
    static final int U1=2;
    static final int U2=3;
    static final int U3=4;

    /**
     * 这里直接列出了了所有小写词、大写词、正常词前后
     * 可能出现的所有前后缀的排列组合
     *
     * 单个注释起头的是可能的情况
     * 双注释起头的是不可能的情况
     */
//    String regNon_wordAsLowerWordSeparator=nonWord+lowerWord+nonWord;
////        String regLowerAsLowerWordSeparator="";
////        String regUpperAsLowerWordSeparator="";
////        String regNon_wordLowerAsLowerWordSeparator="";
//    String regNon_wordUpperAsLowerWordSeparator=nonWord+lowerWord+upperLetter;
////        String regLowerNon_wordAsLowerWordSeparator="";
////        String regLowerUpperAsLowerWordSeparator="";
////        String regUpperNon_wordAsLowerWordSeparator="";
////        String regUpperLowerAsLowerWordSeparator="";
//
//    String regNon_wordAsUpperWordSeparator=nonWord+upperWord+nonWord;
////        String regLowerAsUpperWordSeparator="";
////        String regUpperAsUpperWordSeparator="";
////        String regNon_wordLowerAsUpperWordSeparator="";
//    String regNon_wordUpperAsUpperWordSeparator=nonWord+upperWord+upperLetter+lowerLetter;
//    String regLowerNon_wordAsUpperWordSeparator=lowerLetter+upperWord+nonWord;
//    String regLowerUpperAsUpperWordSeparator=lowerLetter+upperWord+upperLetter+lowerLetter;
////        String regUpperNon_wordAsUpperWordSeparator="";
////        String regUpperLowerAsUpperWordSeparator="";
//
//    String regNon_wordAsNormalWordSeparator=nonWord+normalWord+nonWord;
////        String regLowerAsNormalWordSeparator="";
//    String regUpperAsNormalWordSeparator=upperLetter+normalWord+upperLetter;
////        String regNon_wordLowerAsNormalWordSeparator="";
//    String regNon_wordUpperAsNormalWordSeparator=nonWord+normalWord+upperLetter;
//    String regLowerNon_wordAsNormalWordSeparator=lowerLetter+normalWord+nonWord;
//    String regLowerUpperAsNormalWordSeparator=lowerLetter+normalWord+upperLetter;
//    String regUpperNon_wordAsNormalWordSeparator=upperLetter+normalWord+nonWord;
////        String regUpperLowerAsNormalWordSeparator="";

    private static final String[] arrayRegexs ={
//            下面这三个是单纯的按词分类的表示，但是因为这样分类会导致后续处理不一致，故不再使用
//            nonWord+lowerWord+or(nonWord,upperLetter)----------------------------------纯小写单词
//            or(nonWord,lowerLetter)+upperWord+or(nonWord,upperLetter+lowerLetter)------纯大写单词
//            or(nonWord,lowerLetter,upperLetter)+normalWord+or(nonWord,upperLetter)-----大写开头而后小写的字母,
            nonWord+lowerWord+nonWord,//L1
            nonWord+lowerWord+upperLetter,//L2
            or(or(nonWord,lowerLetter)+upperWord,or(nonWord,lowerLetter,upperLetter)+normalWord)+nonWord,//U1
            or(nonWord,lowerLetter)+upperWord+upperLetter+lowerLetter,//U2
            or(nonWord,lowerLetter,upperLetter)+normalWord+upperLetter//U3
    };
    private static final List<Pattern> patterns= initPatterns(arrayRegexs);//静态预生成pattern
    private static final int matchersCount=patterns.size();//匹配类型的个数

    private String principal;//主体字符串
    private List<Matcher> matchers;//根据主体字符串动态生成的匹配器
    private SortedMap<Integer,Character> elements=new TreeMap<>();//提取出的结果

    public WordMatcherProxy(){this("");}
    public WordMatcherProxy(String principal){
        matchers=new ArrayList<>();
        reset(principal);
    }

    /**
     * 具体功能参考{@link MatcherProxy#extract()}<br/>
     * 这里提供一个对英文单词的提取
     */
    @Override
    public SortedMap<Integer,Character> extract() {
        Matcher matcher;

        //对每一种类型进行匹配
        for(int matcherType=0;matcherType<matchersCount;matcherType++){
            matcher=matchers.get(matcherType);
            while (matcher.find()) {//持续匹配直到匹配不到
                elements.put(//放入 起始位置-字符 的键值对
                        standardStart(matcher.start()),//对起始位置标准化
                        standardCharacter(matcher.start(), matcherType, principal)//对生成的字符标准化
                );
                adjust(matcher,matcherType);//调整matcher的指针位置
            }
        }
        return elements;
    }

    /**
     * 重新设置目标字符串<br/>
     * 注意此处为防止第一个字符（或者最后一个字符）都属于某一个单词而导致无法匹配，
     * 在字符串开头和结尾各加了一个空格，后续处理的时候也注意了这一点
     * @param principal 用于提取的目标字符串
     */
    @Override
    public void reset(String principal) {
        //清除
        this.principal=" "+principal+" ";
        matchers.clear();
        elements.clear();

        //重新生成匹配器
        for (Pattern pattern:patterns){
            matchers.add(pattern.matcher(this.principal));
        }
    }

    /**
     * 对一系列的正则表达式取或<br/>
     * <br/>
     * 如输入"a+","b*","c?",返回"(a+|b*|c?)"
     * @param regexs 输入的一系列正则表达式
     * @return 对这一系列正则表达式取或后的结果
     */
    private static String  or(String... regexs){
        if (regexs.length==0)
            return null;
        String result="(";
        for (int i = 0; i <regexs.length-1 ; i++) {
            result=result.concat(regexs[i]+"|");
        }
        return result.concat(regexs[regexs.length-1]+")");
    }

    /**
     * 根据预设的正则表达式，静态初始化Pattern
     * @param arrayRegexs 预设的正则表达式
     * @return 静态初始化后的Pattern列表
     */
    private static List<Pattern> initPatterns(String[] arrayRegexs){
        List<Pattern> patterns=new ArrayList<>();
        for(String regex:arrayRegexs){
            patterns.add(Pattern.compile(regex));
        }
        return patterns;
    }

    /**
     * 根据匹配词的类型，得到标准化字符<br/>
     * -对纯小写单词，返回起始位置字符的大写<br/>
     * -对纯大写单词和标准单词，直接返回起始位置的字符<br/>
     * @param start 匹配词的起始位置
     * @param matcherType 匹配词的类型
     * @param principal 主体字符串
     * @return 根据匹配词的类型得到的标准化字符输出
     */
    private Character standardCharacter(int start, int matcherType, String principal){
        start+=1;
        switch (matcherType){
            case L1:
            case L2: return Character.toUpperCase(principal.charAt(start));
            case U1:
            case U2:
            case U3: return principal.charAt(start);
            default: throw new NullPointerException();
        }
    }

    /**
     * 对起始位置标准化<br/>
     * 目前直接返回即可，没有逻辑
     * @param start 输入的起始位置
     * @return 标准化后的起始位置
     */
    private int standardStart(int start){
         return start;
    }

    /**
     * 在一次成功的匹配后，根据matcher的类型，将matcher的指针适当前移
     * @param matcher 当前正在进行匹配的matcher
     * @param matcherType 表示matcher的类型见{@linkplain WordMatcherProxy}的{@code L1,L2,U1,U2,U3}
     */
    private void adjust(Matcher matcher,int matcherType){
        int offset;
        switch (matcherType){
            case L1:
            case U1: offset=1;
                break;
            case L2:
            case U2: offset=2;
                break;
            case U3: offset=3;
                break;
            default: throw new NullPointerException();
        }
        matcher.region(matcher.end()-offset,matcher.regionEnd());//region的起始位置设为上一次匹配的超尾索引减去offset,region的结尾不变
    }

}
