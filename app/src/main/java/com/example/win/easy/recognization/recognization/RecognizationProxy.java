package com.example.win.easy.recognization.recognization;

import java.util.List;

public interface RecognizationProxy {

    List<Character> receive(RecognizationUnit unit);

    void clear();

}
