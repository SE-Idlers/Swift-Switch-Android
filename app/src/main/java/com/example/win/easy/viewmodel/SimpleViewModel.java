package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.repository.deprecated.repo.__SongListRepository;
import com.example.win.easy.repository.deprecated.repo.__SongRepository;
import com.example.win.easy.repository.deprecated.repo.__SongXSongListRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleViewModel extends ViewModel {

    private __SongListRepository songListRepository;
    private __SongRepository songRepository;
    private __SongXSongListRepository songXSongListRepository;

    public SimpleViewModel(__SongRepository songRepository,
                           __SongListRepository songListRepository,
                           __SongXSongListRepository songXSongListRepository){
        this.songRepository=songRepository;
        this.songListRepository=songListRepository;
        this.songXSongListRepository=songXSongListRepository;
    }

    private LiveData<List<SongDO>> allSongs;
    private LiveData<List<SongListDO>> allSongLists;
    private LiveData<List<SongXSongListDO>> allRelation;
    private LiveData<Integer> songAmount;
    private LiveData<Integer> songListAmount;
    private Map<Long,LiveData<List<SongDO>>> recordMap;

    public LiveData<List<SongDO>> getAllSongs(){
        if (allSongs==null)
            allSongs=songRepository.getAll();
        return allSongs;
    }

    public LiveData<List<SongListDO>> getAllSongLists(){
        if (allSongLists==null)
            allSongLists=songListRepository.getAll();
        return allSongLists;
    }

    public LiveData<List<SongXSongListDO>> getAllRelation(){
        if (allRelation==null)
            allRelation=songXSongListRepository.getAll();
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

    public LiveData<List<SongDO>> getAllSongsForSongList(SongListDO songListDO){
        if (recordMap==null)
            recordMap=new HashMap<>();
        if (recordMap.get(songListDO.getId())==null)
            recordMap.put(songListDO.getId(),songXSongListRepository.getAllSongsForSongList(songListDO));
        return recordMap.get(songListDO.getId());
    }

    public void insert(SongDO songDO){songRepository.insert(songDO);}
    public void insert(SongListDO songListDO){songListRepository.insert(songListDO);}
    public void insertNewSongAndToSongLists(SongDO newSong, List<SongListDO> songListDOS){
        songXSongListRepository.insertNewSongAndToSongLists(newSong, songListDOS);
    }
}
