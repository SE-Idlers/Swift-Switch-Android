package com.example.win.easy.recognization.recognization;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Adapter implements RecognizationAdapter {
    public static final String model_file = "file:///android_asset/PBfile864.pb";
    public TensorFlowInferenceInterface inferenceInterface;
    private static final String input_node = "reshape_1_input";
    private static final long[] input_shape = {1,784};
    private static final String output_node = "dense_3/Softmax";

    private char[] classes = {
        '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
            'O','P','Q','R','S','T','U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n',
            'o','p','q','r','s','t','u','v','w','x','y','z'
    };
    //使用LinkedHashMap为了使他不按Key排序
    private HashMap<Character, Float> myMap = new LinkedHashMap<>();

    public HashMap<Character, Float> recognize(float[] pb){
        //模型运算
        inferenceInterface.feed(input_node, pb, input_shape);
        inferenceInterface.run(new String[] {output_node});
        float[] result = new float[62];
        inferenceInterface.fetch(output_node, result);

        //后期处理
        float[][] deal_result = new float[62][2];
        //插入key值
        for(int i = 0; i < 62; i++) {
            deal_result[i][0] = i;
            deal_result[i][1] = result[i];
        }
        //插入Map
        for(int i = 0; i < 62; i++){
            myMap.put(classes[(int)deal_result[i][0]], deal_result[i][1]);
        }
        //删除0-9，将a-z加至A-Z
        for(int i = 0; i < 9; i++)
            myMap.remove(classes[i]);
        for(int i = 36; i < 62; i++){
            float toAdd = myMap.get(classes[i]);
            float Adder = myMap.get(classes[i-26]);
            myMap.put(classes[i-26], toAdd+Adder);
            myMap.remove(classes[i]);
        }
        //排序
        List<Map.Entry<Character, Float>> list = new ArrayList<Map.Entry<Character, Float>>(myMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Character, Float>>() {
            @Override
            public int compare(Map.Entry<Character, Float> mapping1, Map.Entry<Character, Float> mapping2) {
                return mapping1.getValue().compareTo(mapping2.getValue());
            }
        });
        //将排序后的list转化为newMap
        HashMap<Character, Float> newMap = new LinkedHashMap<>();
        for(int i = list.size() - 1 ; i >= 0; i--){
            newMap.put(list.get(i).getKey(), list.get(i).getValue());
        }
        myMap = newMap;
        return myMap;
    }
}
