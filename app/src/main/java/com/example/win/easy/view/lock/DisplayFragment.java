package com.example.win.easy.view.lock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.win.easy.R;
import com.example.win.easy.display.interfaces.DisplayService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DisplayFragment extends Fragment {

    @BindView(R.id.start) ImageButton btnPause;
    @BindView(R.id.previous) ImageButton btnPrevious;
    @BindView(R.id.next) ImageButton btnNext;

    private DisplayService displayService;

    public DisplayFragment(DisplayService displayService){
        this.displayService=displayService;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View thisView=inflater.inflate(R.layout.fragment_display,container,false);
        ButterKnife.bind(this,thisView);
        initButtons();
        return thisView;
    }

    private void initButtons(){
        initPauseButton();
        initPreviousButton();
        initNextButton();
    }

    private void initPauseButton() {
        btnPause.setOnClickListener(v -> {
            if (displayService.isPlaying()){
                displayService.pause();
                updateBeginView();
            }else {
                displayService.start();
                updatePauseView();
            }
        });
    }

    private void initPreviousButton() {
        btnPrevious.setOnClickListener(v -> {
            if(!displayService.isPlaying())
                updatePauseView();
            displayService.previous();
        });
    }

    private void initNextButton() {
        btnNext.setOnClickListener(v -> {
            if(!displayService.isPlaying())
                updatePauseView();
            displayService.next();
        });
    }

    private void updateBeginView(){
        btnPause.setImageResource(android.R.drawable.ic_media_play);
    }
    private void updatePauseView(){
        btnPause.setImageResource(android.R.drawable.ic_media_pause);
    }
}
