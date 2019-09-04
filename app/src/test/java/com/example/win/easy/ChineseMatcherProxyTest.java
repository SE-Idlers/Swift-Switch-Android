package com.example.win.easy;

import org.junit.Ignore;

@Ignore
public class ChineseMatcherProxyTest {
//
//    @Spy
//    ChineseCharacterMatcherProxy proxy=new ChineseCharacterMatcherProxy(); //获取proxy实例
//
//    SortedMap<Integer,Character> standard=new TreeMap<>();  //暂存每单个字串的映射
//    List<String> toBeMatchedSamples=new ArrayList<String>();;  //待测字串们
//    List<SortedMap<Integer, Character>> results=new ArrayList<SortedMap<Integer, Character>>();  //保存所有字串的正确映射结果
//
//    @Before
//    public void injectTestSamples(){  //注入测试用例
//        MockitoAnnotations.initMocks(this);
//
//        //本类用于测试：汉字匹配
//        toBeMatchedSamples.add("曲水流觞");//纯汉字
//        standard.put(0,'Q');
//        standard.put(1,'S');
//        standard.put(2,'L');
//        standard.put(3,'S');
//        results.add(new TreeMap<>(standard));
//        standard.clear();
//
//        toBeMatchedSamples.add("曲*水*流*殇"); //汉字+英文符号分隔
//        standard.put(0,'Q');
//        standard.put(2,'S');
//        standard.put(4,'L');
//        standard.put(6,'S');
//        results.add(new TreeMap<>(standard));//**注意：因为上下文关联，下一段代码中的strandard结果应该是一样的，
//                              // 此处没有clear清空，在下一段代码中直接添加strandard到results
//
//        //汉字、字母、数字复合
//        toBeMatchedSamples.add("曲0水1流2觞3 ");//汉字+数字
//        results.add(new TreeMap<>(standard));
//        standard.clear();
//
//        toBeMatchedSamples.add("曲where水are流you觞");//汉字+英语
//        standard.put(0,'Q');
//        standard.put(6,'S');
//        standard.put(10,'L');
//        standard.put(14,'S');
//        results.add(new TreeMap<>(standard));
//        standard.clear();
//
//        toBeMatchedSamples.add("w曲hereA水re20流77觞you");//英语+汉字+数字
//        standard.put(1,'Q');
//        standard.put(7,'S');
//        standard.put(12,'L');
//        standard.put(15,'S');
//        results.add(new TreeMap<>(standard));
//        standard.clear();
//
//        toBeMatchedSamples.add("w曲her'eA水re20流77觞y.ou");//英语+汉字+数字+英文符号
//        standard.put(1,'Q');
//        standard.put(8,'S');
//        standard.put(13,'L');
//        standard.put(16,'S');
//        results.add(new TreeMap<>(standard));
//
//        toBeMatchedSamples.add("w曲her'eA水re20流77觞y。ou");//英语+汉字+数字+英文符号+中文符号
//        results.add(new TreeMap<>(standard));
//        standard.clear(); //清空暂存区
//    }
//
//
//    @Test
//    public void testExtract(){
//        final int samplesAmount=toBeMatchedSamples.size();  //待测用例的数量
//        standard=null;  //用于保存分解字符串的正确结果
//        String currentSample=null;  //用于保存分待测字符串用例
//        for(int sampleIndex=0;sampleIndex<samplesAmount;sampleIndex++){
//            standard=results.get(sampleIndex);  //字符串的正确结果
//            currentSample=toBeMatchedSamples.get(sampleIndex);  //待测字符串
//            proxy.reset(currentSample);  //重置proxy
//            verify(proxy,times(sampleIndex+1)).reset(anyString()); //reset调用成功
//            assertEquals(standard,proxy.extract());  //将分解结果与正确结果比较
//                                                      //assertEquals(Object,Object)，检验知TreeMap已重载equals
//            verify(proxy,times(sampleIndex+1)).extract();
//        }
//    }
}
