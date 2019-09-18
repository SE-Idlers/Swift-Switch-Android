package com.example.win.easy.display;

import com.example.win.easy.view.lock.FailToReplaceSongListException;
import com.example.win.easy.value_object.SongVO;

import java.util.List;

public interface DisplayServiceAdapter {

    void startWith(SongVO songToDisplay, List<SongVO> songList);

    SongVO getCurrentDisplayedSong();

    void replaceSongList(List<SongVO> songList) throws FailToReplaceSongListException;
}
