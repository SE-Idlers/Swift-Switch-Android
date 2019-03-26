package com.example.win.easy;

import com.example.win.easy.filter.WordMatcherProxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.SortedMap;
import java.util.TreeMap;

@RunWith(PowerMockRunner.class)
public class WordMatcherProxyTest {
    WordMatcherProxy proxy=new WordMatcherProxy();
    SortedMap<Integer,Character> standard=new TreeMap<>();

    @Test
    public void test(){

    }
}
