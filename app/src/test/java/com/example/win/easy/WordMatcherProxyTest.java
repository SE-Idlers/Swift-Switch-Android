package com.example.win.easy;

import android.provider.Settings;

import com.example.win.easy.parser.matchers.WordMatcherProxy;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyString;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
public class WordMatcherProxyTest {
    WordMatcherProxy proxy=new WordMatcherProxy();
    SortedMap<Integer,Character> standard=new TreeMap<>();
    List<String> toBeMatchedSamples=new ArrayList<String>();
    List<SortedMap<Integer, Character>> results=new ArrayList<SortedMap<Integer, Character>>();

    @Before
    public void injectTestSamples(){
        //单词匹配
        toBeMatchedSamples.add("Where are you");//空格分隔
        standard.put(0,'W');
        standard.put(6,'A');
        standard.put(10,'Y');
        results.add(standard);        //**注意：因为上下文关联，下一段代码中的strandard结果应该是一样的，
        // 此处没有clear清空，在下一段代码中直接添加strandard到results

        toBeMatchedSamples.add("where are you"); //默认首字母为某个单词的起头
        results.add(standard);

        toBeMatchedSamples.add("Where'are'you");//英文符号分隔
        results.add(standard);

        toBeMatchedSamples.add("Where。are。you"); //中文符号分隔
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("WhereAreYou");//大写分隔
        standard.put(0,'W');
        standard.put(5,'A');
        standard.put(8,'Y');
        results.add(standard);

        toBeMatchedSamples.add("whereAreYou"); //首字母起头+大写分隔
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("whereAre you");//首字母起头+大写分隔+空格分隔
        standard.put(0,'W');
        standard.put(5,'A');
        standard.put(9,'Y');
        results.add(standard);

        toBeMatchedSamples.add("whereAre You"); //首字母起头+大写分隔+空格分隔
        results.add(standard);
        standard.clear();

        //异常
        toBeMatchedSamples.add("Whereareyou");//W
        standard.put(0,'W');
        results.add(standard);

        toBeMatchedSamples.add("whereAreyou");//WA
        standard.put(5,'A');
        results.add(standard);
        standard.clear();

        //复合
        toBeMatchedSamples.add("2077t7702");//数字+英语
        standard.put(4,'t');
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("曲where水are流you觞");//英语+汉字
        standard.put(1,'W');
        standard.put(7,'A');
        standard.put(11,'Y');
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("w曲hereA水re20流77觞you");//英语+汉字+数字
        standard.put(0,'W');
        standard.put(2,'H');
        standard.put(6,'A');
        standard.put(8,'R');
        standard.put(16,'Y');
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("w曲her'eA水re20流77觞y.ou");//英语+汉字+数字+英文符号
        standard.put(0,'W');
        standard.put(2,'H');
        standard.put(6,'E');
        standard.put(7,'A');
        standard.put(9,'R');
        standard.put(17,'Y');
        standard.put(19,'O');
        results.add(standard);

        toBeMatchedSamples.add("w曲her'eA水re20流77觞y。ou");//英语+汉字+数字+英文符号+中文符号
        results.add(standard);
        standard.clear();
    }

    @Test
    public void testExtract(){
        final int samplesAmount=toBeMatchedSamples.size();
        standard=null;
        String currentSample=null;
        for(int sampleIndex=0;sampleIndex<samplesAmount;sampleIndex++){
            standard=results.get(sampleIndex);
            currentSample=toBeMatchedSamples.get(sampleIndex);
            proxy.reset(currentSample);
            verify(proxy).reset(anyString());
            assertEquals(standard,proxy.extract());
            verify(proxy).extract();
        }
    }

    @Test
    @PrepareForTest({WordMatcherProxy.class})
    public void testOr(){
        try{
            String[] str1={};
            assertEquals(null,Whitebox.invokeMethod(proxy,"Or",str1));
            String[] str2={"Joey","Rachel","Ross"};
            assertEquals("(Joey|Rachel|Ross)",Whitebox.invokeMethod(proxy,"Or",str2));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @PrepareForTest({WordMatcherProxy.class})
    public void testStandardCharacter(){
        try{
            String principal="~the openingT T HEMe OvO";
            assertEquals('T',Whitebox.invokeMethod(proxy,"standardCharacter",0,0,principal));//L1
            assertEquals('O',Whitebox.invokeMethod(proxy,"standardCharacter",4,1,principal));//L2
            assertEquals('T',Whitebox.invokeMethod(proxy,"standardCharacter",13,2,principal));//U1
            assertEquals('H',Whitebox.invokeMethod(proxy,"standardCharacter",15,3,principal));//U2
            assertEquals('O',Whitebox.invokeMethod(proxy,"standardCharacter",20,4,principal));//U3
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @PrepareForTest({WordMatcherProxy.class})
    public void testStandardStart(){
        /**/
    }
}
