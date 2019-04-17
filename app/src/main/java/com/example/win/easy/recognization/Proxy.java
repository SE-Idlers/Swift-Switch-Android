package com.example.win.easy.recognization;


<<<<<<< HEAD
import android.gesture.Gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
=======
import java.util.ArrayList;
>>>>>>> 21d3885fdbc0e99edab5864a7a13221a78e189e6
import java.util.List;

public class Proxy implements RecognizationProxy {
    private static List<Character> myList = new ArrayList<>();
<<<<<<< HEAD
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
=======
    private static RecognizationAdapter myAdapter = new Adapter();

    public List<Character> receive(RecognizationUnit unit){
        myAdapter.recognize(((PositionedImage)unit).getFloat_array());
        //TODO
>>>>>>> 21d3885fdbc0e99edab5864a7a13221a78e189e6
        return myList;
    }
    public void clear(){
        myList.clear();
    }
}
