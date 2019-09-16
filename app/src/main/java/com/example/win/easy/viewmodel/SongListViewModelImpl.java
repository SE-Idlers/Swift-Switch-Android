package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.repo.Repo;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.value_object.VOUtil;

import java.util.ArrayList;
import java.util.List;

public class SongListViewModelImpl extends SongListViewModel {

    private Repo repo;
    private VOUtil voUtil;
    private LiveData<List<SongListVO>> allSongList;

    public SongListViewModelImpl(Repo repo, VOUtil voUtil){
        this.repo=repo;
        this.voUtil=voUtil;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LiveData<List<SongListVO>> getAll() {
        //只做转化
        if (allSongList==null){
            allSongList= Transformations.map(repo.getAllSongList(),allSongListDO->{
                List<SongListVO> allSongListVO=new ArrayList<>();
                for (SongListDO songListDO:allSongListDO)
                    allSongListVO.add(voUtil.toVO(songListDO));
                return allSongListVO;
            });
        }
        return allSongList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LiveData<List<SongVO>> songsOf(SongListVO songListVO) {
        //只做转化
        LiveData<List<SongDO>> songsDOLiveData=repo.songsOf(voUtil.toDO(songListVO));
        return Transformations.map(songsDOLiveData,songsDO->{
            List<SongVO> songsVO=new ArrayList<>();
            for (SongDO songDO:songsDO)
                songsVO.add(voUtil.toVO(songDO));
            return songsVO;
        });
    }

    @Override
    public List<SongVO> songsNotIn(SongListVO songListVO) {
        return null;
    }

    @Override
    public void addSongsTo(List<SongVO> songVOs, SongListVO songListVO) {

    }
}


