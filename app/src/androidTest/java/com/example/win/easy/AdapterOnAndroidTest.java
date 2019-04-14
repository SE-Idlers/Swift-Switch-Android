package com.example.win.easy;

import android.support.test.runner.AndroidJUnit4;

import com.example.win.easy.recognization.Adapter;
import com.example.win.easy.recognization.RecognizationAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Map;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class AdapterOnAndroidTest {
    static {
        System.loadLibrary("tensorflow_inference");
    }
    @Test
    public void Test(){

        float[] ttt = new float[62];
        Random r = new Random();
        for(int i = 0 ; i < 62; i++){
            ttt[i] = r.nextFloat();
        }
        RecognizationAdapter test = new Adapter();
        Map<Character, Float> m = test.recognize(ttt);
        test.inferenceInterface = new TensorFlowInferenceInterface(getAssets(),test.model_file);

    }
}
