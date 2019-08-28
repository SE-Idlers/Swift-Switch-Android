package com.example.win.easy;

/*
 * *注意* 此类没有外界依赖，暂时没有用到Mockito
 * 只用了JUnit
 */

public class CharSequenceFilterStrategyTest {
//
//    CharSequenceFilterStrategy filterStrategy=CharSequenceFilterStrategy.getInstance();
//
//    List<List<Character>> sequenceListInResp;//库内已有的序列
//    List<Character> toBeMatchedSample;//单个用例序列
//    List<Integer> matchResult;//结果序列
//
//    @Before //注入测试用例
//    public void injectTestSamples() {
//        //初始化
//        sequenceListInResp = new ArrayList<List<Character>>();
//        matchResult = new ArrayList<Integer>();
//
//        //待匹配字串：ABCD
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('A', 'B')));//前缀匹配，比待匹配字串短
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D')));//前缀匹配，与待匹配字串相等
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D', 'E')));//前缀匹配，比待匹配字串长
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('0', 'A', 'B', 'C', 'D')));//前缀不匹配，后缀匹配
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('0', 'A', '0', 'B', 'C', 'D')));//前缀不匹配
//
//        //待匹配字串：222
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('2', '2')));//前缀匹配，比待匹配字串短
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('2', '2', '2')));//前缀匹配，与待匹配字串相等
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('2', '2', '2', '3')));//前缀匹配，比待匹配字串长
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('r', '2', '2', '2')));//前缀不匹配，后缀匹配
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('r', '2', 'r', '2', '2')));//前缀不匹配
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('2', 'r', '2')));//前缀不匹配
//
//        //待匹配字串：SYBERPUNK2077
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('S','Y','B','E','R','P','P','U','N','K')));
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('S','Y','B','E','R','P','P','U','N','K','2','0','7','7')));
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('S','Y','B','E','R','P','P','U','N','K','2','0','7','7','N','E','V','E','R','C','O','M','E','O','U','T')));
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('N','E','V','E','R','C','O','M','E','O','U','T','S','Y','B','E','R','P','P','U','N','K','2','0','7','7')));
//        sequenceListInResp.add(new ArrayList<Character>(Arrays.asList('N','E','V','E','R','C','O','M','E','O','U','T','2','0','7','7','S','Y','B','E','R','P','P','U','N','K')));
//    }
//
//    @Test
//    public void testTotal(){
//        assertEquals(16,sequenceListInResp.size());//检测库是否有录入错误
//
//        //匹配字母串
//        toBeMatchedSample=new ArrayList<Character>(Arrays.asList('A', 'B', 'C', 'D'));//初始化测试用例
//        matchResult.add(0);//匹配的项
//        matchResult.add(1);
//        matchResult.add(2);
//        assertEquals( matchResult,filterStrategy.filter(toBeMatchedSample,sequenceListInResp));
//        matchResult.clear();
//
//        //匹配数字串
//        toBeMatchedSample=new ArrayList<Character>(Arrays.asList('2', '2', '2'));
//        matchResult.add(5);//匹配的项
//        matchResult.add(6);
//        matchResult.add(7);
//        assertEquals( matchResult,filterStrategy.filter(toBeMatchedSample,sequenceListInResp));
//        matchResult.clear();
//
//        //匹配混合串
//        toBeMatchedSample=new ArrayList<Character>(Arrays.asList('S','Y','B','E','R','P','P','U','N','K','2','0','7','7'));
//        matchResult.add(11);
//        matchResult.add(12);
//        matchResult.add(13);
//        assertEquals( matchResult,filterStrategy.filter(toBeMatchedSample,sequenceListInResp));
//        matchResult.clear();
//    }
}
