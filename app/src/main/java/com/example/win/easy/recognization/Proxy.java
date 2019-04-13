package com.example.win.easy.recognization;


import java.util.ArrayList;
import java.util.List;

public class Proxy implements RecognizationProxy {
    private static List<Character> myList = new ArrayList<>();
    private static RecognizationAdapter myAdapter = new Adapter();

    public List<Character> receive(RecognizationUnit unit){
        myAdapter.recognize(((PositionedImage)unit).getFloat_array());
        //TODO
        return myList;
    }
    public void clear(){
        myList.clear();
    }
}
