package com.example.win.easy;

import org.junit.Ignore;

//没有Mock掉Proxy，因此是一个功能测试
@Ignore
public class RegulationFilenameParserTest {
//    @Spy
//    RegulationFilenameParser parser=RegulationFilenameParser.getInstance();
//    List<MatcherProxy<Character>> matcherProxies=new ArrayList<>();
//    List<String> toBeMatchedSamples=new ArrayList<String>();
//    List<List<Character>> results=new ArrayList<List<Character>>();
//
//    @Before
//    public void injectTestSamples(){
//        MockitoAnnotations.initMocks(this);
//        matcherProxies.add(new ChineseCharacterMatcherProxy());
//        matcherProxies.add(new NumberMatcherProxy());
//        matcherProxies.add(new WordMatcherProxy());
//        parser.setMatcherProxies(matcherProxies);
//        List<Character> tempForCharList;
//        //        ////英文
//        toBeMatchedSamples.add("Where are you");//空格分隔
//        tempForCharList=new ArrayList<Character>(Arrays.asList('W','A','Y'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("where are you"); //默认首字母为某个单词的起头
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("Where'are'you");//英文符号分隔
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("Where。are。you"); //中文符号分隔
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("WhereAreYou");//大写分隔
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("whereAreYou"); //首字母起头+大写分隔
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("whereAre you");//首字母起头+大写分隔+空格分隔
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("whereAre You"); //首字母起头+大写分隔+空格分隔
//        results.add(tempForCharList);
//
//        //异常
//        toBeMatchedSamples.add("Whereareyou");//W
//        tempForCharList=new ArrayList<Character>(Arrays.asList('W'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("whereAreyou");//WA
//        tempForCharList=new ArrayList<Character>(Arrays.asList('W','A'));
//        results.add(tempForCharList);
//
//        ////数字
//        toBeMatchedSamples.add("20777702");//2
//        tempForCharList=new ArrayList<Character>(Arrays.asList('2'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("2077-7702");//英文符号分隔
//        tempForCharList=new ArrayList<Character>(Arrays.asList('2','7'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("2077。7702"); //中文符号分隔
//        results.add(tempForCharList);
//
//        ////汉字
//        toBeMatchedSamples.add("曲水流觞");//完全匹配
//        tempForCharList=new ArrayList<Character>(Arrays.asList('Q','S','L','S'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("曲*水*流*殇"); //英文符号分隔
//        results.add(tempForCharList);
//
//        ////组合
//        toBeMatchedSamples.add("2077t7702");//数字+英语
//        tempForCharList=new ArrayList<Character>(Arrays.asList('2','T','7'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("曲0水1流2觞3 ");//数字+汉字
//        tempForCharList=new ArrayList<Character>(Arrays.asList('Q','0','S','1','L','2','S','3'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("曲where水are流you觞");//英语+汉字
//        tempForCharList=new ArrayList<Character>(Arrays.asList('Q','W','S','A','L','Y','S'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("w曲her'eA水re20流77觞y.ou");//英语+汉字+数字+英文符号
//        tempForCharList=new ArrayList<Character>(Arrays.asList('W','Q','H','E','A','S','R','2','L','7','S','Y','O'));
//        results.add(tempForCharList);
//
//        toBeMatchedSamples.add("w曲her'eA水re20流77觞y。ou");//英语+汉字+数字+英文符号+中文符号
//        results.add(tempForCharList);
//    }
//
//    //**此处没有将parser与proxy解耦，算是功能测试，不是单元测试
//    @Test
//    public void testParse(){ //测试分解函数（public）
//        int samplesAmount=toBeMatchedSamples.size();
//        for(int currentSample=0;currentSample<samplesAmount;++currentSample){
//            assertEquals(results.get(currentSample),parser.parse(toBeMatchedSamples.get(currentSample)));
//            verify(parser,times(currentSample+1)).parse(anyString());
//        }
//    }
}
