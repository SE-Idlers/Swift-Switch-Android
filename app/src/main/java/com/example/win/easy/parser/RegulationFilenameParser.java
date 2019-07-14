package com.example.win.easy.parser;

import com.example.win.easy.parser.interfaces.FilenameParser;
import com.example.win.easy.parser.interfaces.MatcherProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 使用正则表示式将一个歌曲名用一个字符序列表达出来，具体描述见{@link #parse(String)}<br/>
 * 具体的序列生成委托给了一系列的{@link MatcherProxy},本类负责整合所有{@code MatcherProxy}的提取结果，将其转化为一个字符序列
 */
public class RegulationFilenameParser implements FilenameParser<Character> {

    //执行功能的一系列MatcherProxy
    private List<MatcherProxy<Character>> matcherProxies;

    public RegulationFilenameParser(List<MatcherProxy<Character>> matcherProxies){setMatcherProxies(matcherProxies);}

    public void setMatcherProxies(List<MatcherProxy<Character>> matcherProxies) {
        this.matcherProxies = matcherProxies;
    }

    /**
     * 将文件名转化为字符序列<br/>
     * <b>此处文件名必须是去除扩展名与作者后的，如"陈奕迅 - 龙舌兰.mp3"应当处理为" 龙舌兰"后再传入</b>
     * @param filename 输入的文件名
     * @return 转化后的字符序列
     */
    @Override
    public List<Character> parse(String filename) {
        SortedMap<Integer,Character> result=new TreeMap<>();//创建结果集
        for (MatcherProxy<Character> matcherProxy:matcherProxies){//每一个MatcherProxy依次提取
            matcherProxy.reset(filename);//设置主体字符串
            result.putAll(matcherProxy.extract());//将提取结果放入结果集中
        }
        return new ArrayList<>(result.values());//将结果集转化为字符序列
    }
}
