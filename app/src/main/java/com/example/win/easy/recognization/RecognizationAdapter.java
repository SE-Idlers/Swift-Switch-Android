package com.example.win.easy.recognization;

import java.util.SortedMap;

public interface RecognizationAdapter {

    SortedMap<Character, Float> recognize(float[] Float_Array);

}
