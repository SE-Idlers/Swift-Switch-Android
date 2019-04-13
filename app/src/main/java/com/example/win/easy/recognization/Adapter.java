package com.example.win.easy.recognization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class Adapter implements RecognizationAdapter {
    private static final String model_file = "file:///android_asset/PBfile864.pb";
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

    private SortedMap<Character, Float> myMap = new TreeMap<>();

    public SortedMap<Character, Float> recognize(float[] pb){
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
        //排序+插入Map

        for(int i = 0; i < 62; i++){
//            int low_index = i;
//            for(int j = 62 - 1; j > i; j--) {
//                if (deal_result[j][1] > deal_result[low_index][1]) {
//                    low_index = j;
//                }
//                float temp = deal_result[low_index][1];
//                float temp_key = deal_result[low_index][0];
//                deal_result[low_index][1] = deal_result[i][1];
//                deal_result[low_index][0] = deal_result[i][0];
//                deal_result[i][1] = temp;
//                deal_result[i][0] = temp_key;
//            }
            myMap.put(classes[(int)deal_result[i][0]], deal_result[i][1]);
        }
        // 通过ArrayList构造函数把map.entrySet()转换成list
        List<Map.Entry<Character, Float>> list = new ArrayList<Map.Entry<Character, Float>>(myMap.entrySet());
        // 通过比较器实现比较排序
        Collections.sort(list, new Comparator<Map.Entry<Character, Float>>() {
            @Override
            public int compare(Map.Entry<Character, Float> mapping1, Map.Entry<Character, Float> mapping2) {
                return mapping1.getValue().compareTo(mapping2.getValue());
            }
        });
        //筛选
        return myMap;
    }
}
