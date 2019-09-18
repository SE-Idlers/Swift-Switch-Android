package com.example.win.easy.recognization.interfaces;

import android.content.res.AssetManager;

import java.util.List;

public interface RecognitionService {

    List<Character> receive(RecognitionUnit unit);

    void setAssetManager(AssetManager assetManager);

    void clear();

}
