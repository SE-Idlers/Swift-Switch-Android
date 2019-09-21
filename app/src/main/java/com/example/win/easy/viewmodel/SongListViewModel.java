package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.view.main.SongListToCreateAlreadyExistLocallyException;

import java.util.List;

public abstract class SongListViewModel extends ViewModel {

    /**
     * <p>获取所有歌单（本地和网络）</p>
     * @return 可观测的所有歌单数据
     */
    public abstract LiveData<List<SongListVO>> getAll();

    /**
     * <p>获取某个歌单内的所有歌曲</p>
     * @param songListVO 要获取的歌单
     * @return 这个歌单里的歌曲列表
     */
    public abstract List<SongVO> songsOf(SongListVO songListVO);

    public abstract List<SongVO> songsNotIn(SongListVO songListVO);

    public abstract void addSongsTo(List<SongVO> songVOs,SongListVO songListVO);

    public abstract void create(SongListVO songListVO) throws SongListToCreateAlreadyExistLocallyException;
}
