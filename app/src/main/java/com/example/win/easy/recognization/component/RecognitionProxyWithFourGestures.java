package com.example.win.easy.recognization.component;


import android.content.res.AssetManager;

import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.interfaces.Discriminator;
import com.example.win.easy.recognization.interfaces.RecognitionProxy;
import com.example.win.easy.recognization.interfaces.RecognitionUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecognitionProxyWithFourGestures implements RecognitionProxy {
    private static List<Character> myList = new ArrayList<>();
    private static RecognitionAdapterImpl myAdapter;
    private static Discriminator myDecisionMaker = new DecisionMaker();
    private static long CurrentId = 0;

    private static RecognitionProxyWithFourGestures instance=new RecognitionProxyWithFourGestures();
    public static RecognitionProxyWithFourGestures getInstance(){return instance;}
    private RecognitionProxyWithFourGestures(){}

    @Override
    public void setAssetManager(AssetManager assetManger){
        myAdapter=new RecognitionAdapterImpl(assetManger);
    }

    @Override
    public List<Character> receive(RecognitionUnit unit){

        HashMap<Character, Float> myMap = myAdapter.recognize(((PositionedImage) unit).getFloat_array());
        Character character = myDecisionMaker.discriminate(myMap);
//        if(unit instanceof PositionedImage){
        long index = ((PositionedImage)unit).getGestureId();
        if(index != CurrentId){
            myList.add(character);
        }
        else{
            myList.set((int)index, character);
        }
        CurrentId = index;//更新Id
//        }
//        if(unit instanceof Image){
//            myList.add(character);
//        }
        return myList;
    }

    @Override
    public void clear(){
        myList.clear();
    }
}
