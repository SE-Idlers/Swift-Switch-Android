package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.repo.Repo;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.value_object.VOUtil;
import com.example.win.easy.view.main.SongListToCreateAlreadyExistLocallyException;

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
    public LiveData<List<SongListVO>> loadAll() {
        //懒惰加载
        if (allSongList==null)
            allSongList= initAllSongListLiveData();
        return allSongList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SongVO> songsOf(SongListVO songListVO) {
        //获取，避免空值，转化类型
        List<SongDO> songsInRepo=repo.songsOf(voUtil.toDO(songListVO));
        songsInRepo=avoidNullSongs(songsInRepo);
        return toSongsVO(songsInRepo);
    }

    @Override
    public List<SongVO> songsNotIn(SongListVO songListVO) {
        return null;
    }

    @Override
    public void addSongsTo(List<? extends SongVO> songVOs, SongListVO songListVO) {

    }

    @Override
    public void create(SongListVO songListVO) throws SongListToCreateAlreadyExistLocallyException {

    }

    private LiveData<List<SongListVO>> initAllSongListLiveData(){

        LiveData<List<SongListDO>> allSongListDOLiveData=repo.getAllSongList();

        //空值避免，类型转换
        return Transformations.map(allSongListDOLiveData,allSongListFromRepo->{
            List<SongListDO> allSongListDO= avoidNullSongLists(allSongListFromRepo);
            return toAllSongListVO(allSongListDO);
        });
    }

    private List<SongListDO> avoidNullSongLists(List<SongListDO> songListDOs){
        return songListDOs==null
                ? new ArrayList<>()
                :songListDOs;
    }

    private List<SongListVO> toAllSongListVO(List<SongListDO> songListDOs){
        List<SongListVO> allSongListVO=new ArrayList<>();
        for (SongListDO songListDO:songListDOs)
            allSongListVO.add(voUtil.toVO(songListDO));
        return allSongListVO;
    }

    private List<SongDO> avoidNullSongs(List<SongDO> songDOs){
        return songDOs==null
                ?new ArrayList<>()
                :songDOs;
    }

    private List<SongVO> toSongsVO(List<SongDO> songDOs){
        List<SongVO> songsVO=new ArrayList<>();
        for (SongDO songDO:songDOs)
            songsVO.add(voUtil.toVO(songDO));
        return songsVO;
    }
}


