package com.example.win.easy.repository;

import androidx.lifecycle.LiveData;

import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;

import java.util.List;

public interface SongsOfSongListService {

    LiveData<List<SongVO>> songsOf(SongListVO songListVO);

}
