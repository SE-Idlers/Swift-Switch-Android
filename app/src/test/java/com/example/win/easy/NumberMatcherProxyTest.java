package com.example.win.easy;

import com.example.win.easy.parser.matchers.NumberMatcherProxy;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

import org.powermock.modules.junit4.PowerMockRunner;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

@RunWith(PowerMockRunner.class)
public class NumberMatcherProxyTest {
    NumberMatcherProxy proxy=new NumberMatcherProxy();
    SortedMap<Integer,Character> standard=new TreeMap<>();
    List<String> toBeMatchedSamples=new ArrayList<String>();
    List<SortedMap<Integer, Character>> results=new ArrayList<SortedMap<Integer, Character>>();

    @Before
    public void injectTestSamples(){
        //数字匹配
        toBeMatchedSamples.add("20777702");//2
        standard.put(0,'2');
        results.add(standard);        //**注意：因为上下文关联，下一段代码中的strandard结果应该是一样的，
                                     // 此处没有clear清空，在下一段代码中直接添加strandard到results

        toBeMatchedSamples.add("2077-7702");//英文符号分隔
        standard.put(7,'5');
        results.add(standard);

        toBeMatchedSamples.add("2077。7702"); //中文符号分隔
        results.add(standard);

        //复合
        toBeMatchedSamples.add("2077t7702");//数字+英语
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("曲0水1流2觞3");//数字+汉字
        standard.put(1,'0');
        standard.put(3,'1');
        standard.put(5,'2');
        standard.put(7,'3');
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("w曲hereA水re20流77觞you");//英语+汉字+数字
        standard.put(10,'2');
        standard.put(13,'7');
        results.add(standard);
        standard.clear();

        toBeMatchedSamples.add("w曲her'eA水re20流77觞y.ou");//英语+汉字+数字+英文符号
        standard.put(11,'2');
        standard.put(14,'7');
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
}
