package com.example.win.easy.recognization;

import java.util.HashMap;
import java.util.Set;

public class DecisionMaker implements Discriminator {
    private double Threshold = 0.5;
    public Character discriminate(HashMap<Character, Float> Probability){
        //TODO: 以后要根据阈值决策，这里暂时返回最高概率的
        Set<Character> myset = Probability.keySet();
        Character[] myarr = (Character[])myset.toArray();
        return myarr[0];
    }
}
