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
    private boolean has0 = false, has1 = false, has2 = false, has3 = false;

    @Override
    public void setAssetManager(AssetManager assetManger){
        myAdapter=new RecognitionAdapterImpl(assetManger);
    }

    @Override
    public List<Character> receive(RecognitionUnit unit){
        //根据bitmap转换出的float_array识别
        HashMap<Character, Float> myMap = myAdapter.recognize(((PositionedImage) unit).getFloat_array());
        //从Map中决策出概率最高字符
        Character character = myDecisionMaker.discriminate(myMap);

        //TODO: PositionedImage过于无用，仅用于demo实现
        long index = ((PositionedImage)unit).getGestureId();
        switch ((int)index){
            case 0:
                if(!has0){
                    myList.add((int)index, character);
                    has0 = true;
                }
                else{
                    myList.set((int)index, character);
                }
                break;
            case 1:
                if(has0){
                    if(!has1){
                        myList.add((int)index, character);
                        has1 = true;
                    }
                    else{
                        myList.set((int)index, character);
                    }
                }
                break;
            case 2:
                if(has0&&has1){
                    if(!has2){
                        myList.add((int)index, character);
                        has2 = true;
                    }
                    else{
                        myList.set((int)index, character);
                    }
                }
                break;
            case 3:
                if(has0&&has1&&has2){
                    if(!has3){
                        myList.add((int)index, character);
                        has3 = true;
                    }
                    else{
                        myList.set((int)index, character);
                    }
                }
                break;
        }
        CurrentId = index;//更新Id
        return myList;
    }

    @Override
    public void clear(){
        myList.clear();
        has0 = has1 = has2 = has3 = false;
    }
}
