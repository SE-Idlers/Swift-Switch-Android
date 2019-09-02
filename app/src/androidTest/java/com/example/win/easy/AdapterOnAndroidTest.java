package com.example.win.easy;


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4ClassRunner.class)
public class AdapterOnAndroidTest {
    static {
        System.loadLibrary("tensorflow_inference");
    }
    @Test
    public void Test(){

//        float[] ttt = new float[62];
//        Random r = new Random();
//        for(int i = 0 ; i < 62; i++){
//            ttt[i] = r.nextFloat();
//        }
//        RecognitionAdapter test = new RecognitionAdapterImpl();
//        Map<Character, Float> m = test.recognize(ttt);
//        test.inferenceInterface = new TensorFlowInferenceInterface(getAssets(),test.model_file);

    }
}
