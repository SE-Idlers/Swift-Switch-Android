package com.example.win.easy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.repository.repo.SongListRepository;
import com.example.win.easy.repository.repo.SongRepository;
import com.example.win.easy.repository.repo.SongXSongListRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleViewModel extends ViewModel {

    private SongListRepository songListRepository;
    private SongRepository songRepository;
    private SongXSongListRepository songXSongListRepository;

    public SimpleViewModel(SongRepository songRepository,
                           SongListRepository songListRepository,
                           SongXSongListRepository songXSongListRepository){
        this.songRepository=songRepository;
        this.songListRepository=songListRepository;
        this.songXSongListRepository=songXSongListRepository;
    }

    private LiveData<List<SongPojo>> allSongs;
    private LiveData<List<SongListPojo>> allSongLists;
    private LiveData<List<SongXSongList>> allRelation;
    private LiveData<Integer> songAmount;
    private LiveData<Integer> songListAmount;
    private Map<Long,LiveData<List<SongPojo>>> recordMap;

    public LiveData<List<SongPojo>> getAllSongs(){
        if (allSongs==null)
            allSongs=songRepository.getAll();
        return allSongs;
    }

    public LiveData<List<SongListPojo>> getAllSongLists(){
        if (allSongLists==null)
            allSongLists=songListRepository.getAll();
        return allSongLists;
    }

    public LiveData<List<SongXSongList>> getAllRelation(){
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

    public LiveData<List<SongPojo>> getAllSongsForSongList(SongListPojo songListPojo){
        if (recordMap==null)
            recordMap=new HashMap<>();
        if (recordMap.get(songListPojo.getId())==null)
            recordMap.put(songListPojo.getId(),songXSongListRepository.getAllSongsForSongList(songListPojo));
        return recordMap.get(songListPojo.getId());
    }

    public void insert(SongPojo songPojo){songRepository.insert(songPojo);}
    public void insert(SongListPojo songListPojo){songListRepository.insert(songListPojo);}
    public void insertNewSongAndToSongLists(SongPojo newSong,List<SongListPojo> songListPojos){
        songXSongListRepository.insertNewSongAndToSongLists(newSong,songListPojos);
    }
}
