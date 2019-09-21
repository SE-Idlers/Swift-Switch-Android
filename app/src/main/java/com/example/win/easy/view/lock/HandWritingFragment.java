package com.example.win.easy.view.lock;

import android.Manifest;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.win.easy.R;
import com.example.win.easy.recognization.PositionedImage;
import com.example.win.easy.recognization.interfaces.RecognitionService;

import java.util.ArrayList;
import java.util.List;


public class HandWritingFragment extends Fragment {

    private Integer[] gestureBoardIds =new Integer[]{
            R.id.gesture1,
            R.id.gesture2,
            R.id.gesture3,
            R.id.gesture4
    };
    private List<GestureOverlayView> gestureBoards=new ArrayList<>();
    private MutableLiveData<List<Character>> recognitionLiveData =new MutableLiveData<>();

    private RecognitionService recognitionService;
    public HandWritingFragment(RecognitionService recognitionService){
        this.recognitionService=recognitionService;
    }

    public void getObserved(LifecycleOwner lifecycleOwner, Observer<? super List<Character>> observer){
        recognitionLiveData.observe(lifecycleOwner,observer);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView=inflater.inflate(R.layout.fragment_hand_writing,container,false);

        setUpRecognitionAsset();
        setUpGestureBoardsOf(thisView);

        return thisView;
    }

    private void setUpRecognitionAsset(){
        recognitionService.setAssetManager(getActivity().getAssets());
    }

    private void setUpGestureBoardsOf(View view){
        for (Integer gestureBoardId : gestureBoardIds) {
            GestureOverlayView gestureBoard = view.findViewById(gestureBoardId);
            setUpGestureBoard(gestureBoard);
            gestureBoards.add(gestureBoard);
        }
    }

    private void setUpGestureBoard(GestureOverlayView gestureBoard){
        gestureBoard.setGestureStrokeWidth(15);
        gestureBoard.addOnGesturePerformedListener((thisBoard, performedGesture) -> {
            if (authorityPermitted()){
                List<Character> result=recognize(thisBoard,performedGesture);
                notifyObserver(result);
            }else
                requestPermission();
        });
    }

    private boolean authorityPermitted(){
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
    }

    private List<Character> recognize(GestureOverlayView thisBoard, Gesture gesture){
        long indexOfThisBoard=gestureBoards.indexOf(thisBoard);
        PositionedImage imageToRecognize=PositionedImage.create(gesture,indexOfThisBoard);
        return recognitionService.receive(imageToRecognize);
    }

    private void notifyObserver(List<Character> recolonizationResult){
        recognitionLiveData.setValue(recolonizationResult);
    }
}
