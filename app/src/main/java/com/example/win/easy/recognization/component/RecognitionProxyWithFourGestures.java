package com.example.win.easy.recognization.component;


import android.content.res.AssetManager;

import com.example.win.easy.recognization.Image;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.interfaces.Discriminator;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.recognization.interfaces.RecognitionUnit;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RecognitionProxyWithFourGestures implements RecognitionProxy {
    private static List<Character> myList = new ArrayList<>();
    private static HashMap<Character, Float> myMap = new LinkedHashMap<>();
    private static RecognitionAdapterImpl myAdapter;
    private static Discriminator myDecisionMaker = new DecisionMaker();
    private static int CurrentId = 0;

    public RecognitionProxyWithFourGestures(AssetManager assetManager){
        myAdapter=new RecognitionAdapterImpl(new TensorFlowInferenceInterface(assetManager,RecognitionAdapterImpl.model_file));
    }

    private RecognitionProxyWithFourGestures(){}
    public List<Character> receive(RecognitionUnit unit){

        //需要在能调用getAssets()的地方加上下列语句
        //adapter.inferenceInterface = new TensorFlowInferenceInterface(getAssets(),adapter.model_file);
        myMap = myAdapter.recognize(((PositionedImage)unit).getFloat_array());
        Character character = myDecisionMaker.discriminate(myMap);
        if(unit instanceof PositionedImage){
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
            myList.add(character);
        }
        return myList;
    }
    public void clear(){
        myList.clear();
    }
}
