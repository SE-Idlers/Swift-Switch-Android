package com.example.win.easy.recognization;

import java.util.SortedMap;

public interface Discriminator {

    Character discriminate(SortedMap<Character,Double> softmaxProbability);

}
