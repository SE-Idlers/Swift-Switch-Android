package com.example.win.easy.recognization.recognization;

import java.util.HashMap;

public interface Discriminator {

    Character discriminate(HashMap<Character, Float> Probability);

}
