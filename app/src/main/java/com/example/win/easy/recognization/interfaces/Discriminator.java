package com.example.win.easy.recognization.interfaces;

import java.util.HashMap;

public interface Discriminator {

    Character discriminate(HashMap<Character, Float> Probability);

}
