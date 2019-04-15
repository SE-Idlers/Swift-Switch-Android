package com.example.win.easy.recognization;


import android.gesture.Gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Proxy implements RecognizationProxy {
    private static List<Character> myList = new ArrayList<>();
    private static HashMap<Character, Float> myMap = new LinkedHashMap<>();
    private static RecognizationAdapter myAdapter = new Adapter();
    private static Discriminator myDecisionMaker = new DecisionMaker();
    private static int CurrentId = 0;
    private static RecognizationUnit myImage;

    public boolean loadGesture(Gesture gesture, int positionId){
        //下列语句可更改
        myImage = new PositionedImage();
        ((PositionedImage) myImage).create(gesture, positionId);
        return (((PositionedImage) myImage).getBitmap()!=null);
    }

    public List<Character> receive(RecognizationUnit unit){
        //需要在能调用getAssets()的地方加上下列语句
        //adapter.inferenceInterface = new TensorFlowInferenceInterface(getAssets(),adapter.model_file);
        if(unit instanceof PositionedImage){
            myMap = myAdapter.recognize(((PositionedImage)unit).getFloat_array());
            Character character = myDecisionMaker.discriminate(myMap);
            int index = ((PositionedImage)unit).getGestureId();
            if(index != CurrentId){
                myList.add(character);
            }
            else{
                myList.set(index, character);
            }
            CurrentId = index;//更新Id
        }
        if(unit instanceof Image){
            myMap = myAdapter.recognize(((Image)unit).getFloat_array());
            Character character = myDecisionMaker.discriminate(myMap);
            myList.add(character);
        }
        return myList;
    }
    public void clear(){
        myList.clear();
    }
}
