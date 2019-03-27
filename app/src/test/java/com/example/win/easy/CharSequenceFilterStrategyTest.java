package com.example.win.easy;

import com.example.win.easy.filter.CharSequenceFilterStrategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class CharSequenceFilterStrategyTest {

    @Spy CharSequenceFilterStrategy filterStrategy=new CharSequenceFilterStrategy();
    List<File> fileList;

    @Before
    //注入测试用例
    public void injectTestSamples(){
        fileList=new ArrayList<>();
        fileList.add(new File("Current/陈奕迅 - 路…一直都在.mp3"));//特殊标点示例
        fileList.add(new File("ElsePath/陈奕迅 - 对不起 谢谢.mp3"));//中字空格示例
        fileList.add(new File("GoF/陈奕迅 - 低等动物 (Live).mp3"));//括号示例
        fileList.add(new File("Ng/陈奕迅 - 那一夜有没有说.mp3"));//正常示例
        fileList.add(new File("TheShy/陈奕迅 - 16月6日晴.mp3"));//掺杂数字示例
        fileList.add(new File("Ming/Lady Gaga - Always Remember Us This Way.mp3"));//全英示例

    }

    @Test
    public void testTotal(){

    }
}
