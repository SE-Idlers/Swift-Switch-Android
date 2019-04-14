package com.example.win.easy.recognization;

import java.util.List;

public interface RecognitionProxy {

    List<Character> receive(RecognitionUnit unit);

    void clear();

}
