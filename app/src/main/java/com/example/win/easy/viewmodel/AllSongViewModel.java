package com.example.win.easy.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.repository.deprecated.repo.__SongListRepository;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.repository.deprecated.repo.__SongXSongListRepository;
import com.example.win.easy.value_object.SongListVO;
import com.example.win.easy.value_object.SongVO;
import com.example.win.easy.value_object.SongXSongListVO;
import com.example.win.easy.value_object.VOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
/*
改于SimpleViewModel,为AllSongFragment提供数据
 */
public class AllSongViewModel extends ViewModel {

    private __SongListRepository songListRepository;
    private __SongRepository songRepository;
    private __SongXSongListRepository songXSongListRepository;

    private VOUtil mVOUtil;

    public AllSongViewModel(__SongRepository songRepository,
                           __SongListRepository songListRepository,
                           __SongXSongListRepository songXSongListRepository){
        this.songRepository=songRepository;
        this.songListRepository=songListRepository;
        this.songXSongListRepository=songXSongListRepository;
    }

    private LiveData<List<SongVO>> allSongs;
    private LiveData<List<SongListVO>> allSongLists;
    private LiveData<List<SongXSongListVO>> allRelation;
    private LiveData<Integer> songAmount;
    private LiveData<Integer> songListAmount;
    private Map<Long,LiveData<List<SongVO>>> recordMap;

    /*
    拿回所有给ALLSongViewModel的歌曲(VO形式)
     */
    public LiveData<List<SongVO>> getAllSongs(){

        //TODO 从仓库获取DO转换成DO,还是仓库中有VO？？进行了一系列将仓库中VO转换成DO的尝试，失败

        /*
        List<SongDO> tempAllSongs;
        LiveData<ArrayList<SongVO>> allSongs=new LiveData<ArrayList<SongVO>>;
        ArrayList<SongVO> allSongsreal=new ArrayList<>();
        tempAllSongs=songRepository.getAll().getValue();

        for (SongDO tempAllSong: tempAllSongs){
            allSongsreal.add(mVOUtil.toVO(tempAllSong));

        }
        allSongs=allSongsreal;

        /*
        if (allSongs==null)
            //

          */
        return allSongs;
    }

    public LiveData<List<SongListVO>> getAllSongLists(){
        /*if (allSongLists==null)
            allSongLists=songListRepository.getAll();*/
        return allSongLists;
    }

    public LiveData<List<SongXSongListVO>> getAllRelation(){
        /*if (allRelation==null)
            allRelation=songXSongListRepository.getAll();*/
        return allRelation;
    }

    public LiveData<Integer> getSongAmount(){
        if (songAmount==null)
            songAmount=Transformations.map(getAllSongs(),List::size);
        return songAmount;
    }

    public LiveData<Integer> getSongListAmount(){
        if (songListAmount==null)
            songListAmount=Transformations.map(getAllSongLists(),List::size);
        return songListAmount;
    }

    public LiveData<List<SongVO>> getAllSongsForSongList(SongListVO songListVO){
        if (recordMap==null)
            recordMap=new HashMap<>();
        if (recordMap.get(songListVO.getId())==null)
            return null;
            //recordMap.put(songListVO.getId(),mVOUtil.toVO(songXSongListRepository.getAllSongsForSongList(mVOUtil.toDO(songListVO)).getValue()));
        return recordMap.get(songListVO.getId());
    }

    public void insert(SongVO songVO){songRepository.insert(mVOUtil.toDO(songVO));}
    public void insert(SongListVO songListVO){songListRepository.insert(mVOUtil.toDO(songListVO));}
    public void insertNewSongAndToSongLists(SongVO newSong, List<SongListVO> songListVOS){
        ArrayList<SongListDO> tempSongListVOS=new ArrayList<>();
        for(SongListVO songListVO : songListVOS){
            tempSongListVOS.add(mVOUtil.toDO(songListVO));
        }
        songXSongListRepository.insertNewSongAndToSongLists(mVOUtil.toDO(newSong),tempSongListVOS);
    }
}
