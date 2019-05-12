package com.example.win.easy.view;

import android.view.View;
import android.widget.ImageButton;

import com.example.win.easy.R;
import com.example.win.easy.activity.LockActivity;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;

/**
 * 控制播放器视图及监听
 */
public class MediaPlayerView {

    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    private static MediaPlayerView instance =new MediaPlayerView();
    public static MediaPlayerView getInstance(){return instance;}
    /**
     * 设置按钮监听
     */
    private MediaPlayerView() {
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        final ImageButton btnPrevious=LockActivity.lockActivity.findViewById(R.id.previous);
        final ImageButton btnNext=LockActivity.lockActivity.findViewById(R.id.next);

        //播放、暂停
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayManager.isPlaying()){
                    displayManager.pause();
                    btnPause.setImageResource(android.R.drawable.ic_media_play);
                }else {
                    displayManager.start();
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
        //前一首
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.previous();
            }
        });
        //后一首
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!displayManager.isPlaying())
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                displayManager.next();
            }
        });
    }

    public void updateBeginView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_play);
    }
    public void updatePauseView(){
        final ImageButton btnPause= LockActivity.lockActivity.findViewById(R.id.start);
        btnPause.setImageResource(android.R.drawable.ic_media_pause);
    }
}

