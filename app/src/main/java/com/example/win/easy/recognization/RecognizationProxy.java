package com.example.win.easy.recognization;

import java.util.List;

public interface RecognizationProxy {

    List<Character> receive(RecognizationUnit unit);

    void clear();

}
