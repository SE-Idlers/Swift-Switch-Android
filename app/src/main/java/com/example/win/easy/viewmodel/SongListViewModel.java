package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.SongsOfSongListService;
import com.example.win.easy.value_object.SongListVO;

import java.util.List;

public abstract class SongListViewModel extends ViewModel implements SongsOfSongListService {

    public abstract LiveData<List<SongListVO>> getAll();


}
