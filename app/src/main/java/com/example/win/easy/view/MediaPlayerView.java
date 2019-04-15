package com.example.win.easy.view;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

import com.example.win.easy.activity.MainActivity;
import com.example.win.easy.R;
import com.example.win.easy.display.DisplayManagerImpl;
import com.example.win.easy.display.interfaces.DisplayManager;

public class MediaPlayerView {
    private DisplayManager displayManager=DisplayManagerImpl.getInstance();

    private MediaPlayer mediaPlayer=new MediaPlayer();

    private static ImageButton btnPause= MainActivity.mainActivity.findViewById(R.id.start);
    private static ImageButton btnPrevious=MainActivity.mainActivity.findViewById(R.id.previous);
    private static ImageButton btnNext=MainActivity.mainActivity.findViewById(R.id.next);
    private static MediaPlayerView instance =new MediaPlayerView();
    public static MediaPlayerView getInstance(){return instance;}
    private MediaPlayerView(){}
    static {

    }

    private  void AdministrateButton() {
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
}

