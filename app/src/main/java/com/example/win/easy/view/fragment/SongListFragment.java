package com.example.win.easy.view.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.win.easy.display.interfaces.DisplayManager;
import com.example.win.easy.repository.SongsOfSongListService;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.EntityItem;
import com.example.win.easy.view.OnClickFunc;

public class SongListFragment extends ListFragment {

    private SongListVO thisSongList;
    private DisplayManager displayManager;
    private SongsOfSongListService songsOfSongListService;

    public SongListFragment(DisplayManager displayManager,SongsOfSongListService songsOfSongListService){
        this.displayManager=displayManager;
        this.songsOfSongListService=songsOfSongListService;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisSongList=SongListFragmentArgs.fromBundle(getArguments()).getSelectedSongList();
        songsOfSongListService.songsOf(thisSongList).observe(this,songs->{

            for (SongVO songVO:songs)
                new SongItem(songVO,(songOfThis,thisView)->{

                });



        });

    }


    class SongItem extends EntityItem<SongVO> {

        public SongItem(SongVO entity, OnClickFunc<SongVO> onClickFunc) {
            super(SongListFragment.this.getContext(), entity, onClickFunc);
            init();
        }

        private void init(){

        }
    }


}
